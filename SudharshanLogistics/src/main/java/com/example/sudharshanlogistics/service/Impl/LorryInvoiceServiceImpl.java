package com.example.sudharshanlogistics.service.Impl;

import com.example.sudharshanlogistics.service.EmailService;
import com.example.sudharshanlogistics.util.EmailTemplateUtil;
import com.example.sudharshanlogistics.util.PdfGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.sudharshanlogistics.entity.Item;
import com.example.sudharshanlogistics.entity.LorryInvoice;
import com.example.sudharshanlogistics.entity.Route;
import com.example.sudharshanlogistics.entity.Party;
import com.example.sudharshanlogistics.entity.Branch;
import com.example.sudharshanlogistics.entity.Vehicle;
import com.example.sudharshanlogistics.entity.Owner;
import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryInvoiceDetailsResponse;
import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptRequestDto;
import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptResponseDto;
import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;
import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.repository.ItemRepository;
import com.example.sudharshanlogistics.repository.LorryInvoiceRepository;
import com.example.sudharshanlogistics.repository.PartyRepository;
import com.example.sudharshanlogistics.repository.RouteRepository;
import com.example.sudharshanlogistics.repository.VehicleRepository;
import com.example.sudharshanlogistics.service.LorryInvoiceService;
import com.example.sudharshanlogistics.service.TaxConfigurationService;
import com.example.sudharshanlogistics.service.WayBillGenerator;
import com.example.sudharshanlogistics.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LorryInvoiceServiceImpl implements LorryInvoiceService {

    private final LorryInvoiceRepository lorryReceiptRepository;
    private final RouteRepository routeRepository;
    private final ItemRepository itemRepository;
    private final PartyRepository partyRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;
    private final WayBillGenerator wayBillGenerator;
    private final TaxConfigurationService taxConfigurationService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public LorryReceiptResponseDto createLorryReceipt(LorryReceiptRequestDto dto) {
        List<Item> items = dto.getItemIds().stream()
                .map(id -> itemRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Item not found with ID: " + id)))
                .collect(Collectors.toList());

        LorryInvoice.ModeOfPayment mop = LorryInvoice.ModeOfPayment.valueOf(dto.getModeOfPayment());

        BigDecimal subTotal = dto.getRatePerKg().multiply(BigDecimal.valueOf(dto.getActualWeight()));

        // Fetch latest tax configuration
        var taxConfig = taxConfigurationService.getLatestTaxConfig();
        BigDecimal cgst = subTotal.multiply(taxConfig.getCgstPercent()).divide(BigDecimal.valueOf(100));
        BigDecimal sgst = subTotal.multiply(taxConfig.getSgstPercent()).divide(BigDecimal.valueOf(100));
        BigDecimal igst = subTotal.multiply(taxConfig.getIgstPercent()).divide(BigDecimal.valueOf(100));
        BigDecimal grandTotal = subTotal.add(cgst).add(sgst).add(igst);

        LorryInvoice lr = LorryInvoice.builder()
                .wayBillNo(wayBillGenerator.generateWayBillNo())
                .date(dto.getDate())
                .collType(dto.getCollType())
                .delvType(dto.getDelvType())
                .gcType(dto.getGcType())
                .modeOfPayment(mop)
                .freightPayBy(dto.getFreightPayBy())
                .actualWeight(dto.getActualWeight())
                .ratePerKg(dto.getRatePerKg())
                .totalPackages(dto.getTotalPackages())
                .invoiceValue(dto.getInvoiceValue())
                .subTotal(subTotal)
                .igstTax(igst)
                .cgstTax(cgst)
                .sgstTax(sgst)
                .grandTotal(grandTotal)
                .status(LorryInvoice.Status.CREATED)
                .build();

        // Detach items from previous LRs if any
        for (Item item : items) {
            LorryInvoice previousLr = item.getLorryInvoice();
            if (previousLr != null) {
                previousLr.removeItem(item);
                lorryReceiptRepository.save(previousLr);
            }
        }

        items.forEach(item -> item.setLorryInvoice(lr));

        LorryInvoice saved = lorryReceiptRepository.save(lr);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public LorryReceiptResponseDto updateLorryReceipt(UUID id, LorryReceiptRequestDto dto) {
        LorryInvoice lr = lorryReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lorry Receipt not found with ID: " + id));

        // Update date if provided
        if (dto.getDate() != null) {
            lr.setDate(dto.getDate());
        }

        // Handle items: if itemIds provided, replace; else keep existing
        List<Item> newItems = List.of();
        Route route = null;
        if (dto.getItemIds() != null && !dto.getItemIds().isEmpty()) {
            // Fetch new items
            newItems = dto.getItemIds().stream()
                    .map(itemId -> itemRepository.findById(itemId)
                            .orElseThrow(() -> new NotFoundException("Item not found with ID: " + itemId)))
                    .collect(Collectors.toList());

            // Validate route
            route = newItems.get(0).getRoute();
            if (route == null)
                throw new IllegalArgumentException("Item does not have an associated route");
            UUID routeId = route.getId();
            for (Item item : newItems) {
                if (!routeId.equals(item.getRoute().getId())) {
                    throw new IllegalArgumentException("All items must belong to the same route");
                }
            }
        } else {
            // Keep existing items
            newItems = itemRepository.findByLorryInvoice_Id(lr.getId());
        }

        // Update LR scalar fields
        lr.setCollType(dto.getCollType());
        lr.setDelvType(dto.getDelvType());
        lr.setGcType(dto.getGcType());
        lr.setModeOfPayment(dto.getModeOfPayment() != null ? LorryInvoice.ModeOfPayment.valueOf(dto.getModeOfPayment())
                : lr.getModeOfPayment());
        lr.setFreightPayBy(dto.getFreightPayBy());
        lr.setActualWeight(dto.getActualWeight());
        lr.setRatePerKg(dto.getRatePerKg());
        lr.setTotalPackages(dto.getTotalPackages());
        lr.setInvoiceValue(dto.getInvoiceValue());

        if (dto.getRatePerKg() != null && dto.getActualWeight() != null) {
            BigDecimal subTotal = dto.getRatePerKg().multiply(BigDecimal.valueOf(dto.getActualWeight()));
            lr.setSubTotal(subTotal);

            // Recalculate taxes
            var taxConfig = taxConfigurationService.getLatestTaxConfig();
            BigDecimal cgst = subTotal.multiply(taxConfig.getCgstPercent()).divide(BigDecimal.valueOf(100));
            BigDecimal sgst = subTotal.multiply(taxConfig.getSgstPercent()).divide(BigDecimal.valueOf(100));
            BigDecimal igst = subTotal.multiply(taxConfig.getIgstPercent()).divide(BigDecimal.valueOf(100));
            BigDecimal grandTotal = subTotal.add(cgst).add(sgst).add(igst);

            lr.setCgstTax(cgst);
            lr.setSgstTax(sgst);
            lr.setIgstTax(igst);
            lr.setGrandTotal(grandTotal);
        }

        // Handle items replacement only if new items provided
        if (dto.getItemIds() != null && !dto.getItemIds().isEmpty()) {
            // Detach current items from this LR
            List<Item> currentItems = itemRepository.findByLorryInvoice_Id(lr.getId());
            for (Item item : currentItems) {
                item.setLorryInvoice(null);
                itemRepository.save(item);
            }

            // Detach new items from previous LRs if any and attach to this LR
            for (Item newItem : newItems) {
                LorryInvoice previousLr = newItem.getLorryInvoice();
                if (previousLr != null && !previousLr.getId().equals(lr.getId())) {
                    previousLr.removeItem(newItem);
                    lorryReceiptRepository.save(previousLr);
                }
                newItem.setLorryInvoice(lr);
                itemRepository.save(newItem);
            }
        }

        LorryInvoice updated = lorryReceiptRepository.save(lr);
        return mapToDto(updated);
    }

    @Override
    public LorryReceiptResponseDto getLorryReceiptById(UUID id) {
        LorryInvoice lr = lorryReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lorry Receipt not found with ID: " + id));
        return mapToDto(lr);
    }

    @Override
    public LorryReceiptResponseDto getLorryReceiptByWayBillNo(String wayBillNo) {
        LorryInvoice lr = lorryReceiptRepository.findByWayBillNo(wayBillNo)
                .orElseThrow(() -> new NotFoundException("LorryReceipt not found with WayBillNo: " + wayBillNo));
        return mapToDto(lr);
    }

    @Override
    public Page<LorryReceiptResponseDto> getAllLorryReceipts(Pageable pageable) {
        Page<LorryInvoice> lrs = lorryReceiptRepository.findAll(pageable);
        return lrs.map(this::mapToDto);
    }

    @Override
    public LorryReceiptResponseDto updateLorryReceiptStatus(UUID id, String status) {
        LorryInvoice lr = lorryReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LorryReceipt not found with ID: " + id));
        lr.setStatus(LorryInvoice.Status.valueOf(status));
        LorryInvoice saved = lorryReceiptRepository.save(lr);
        return mapToDto(saved);
    }

    @Override
    public void deleteLorryReceipt(UUID id) {
        LorryInvoice lr = lorryReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LorryReceipt not found with ID: " + id));
        // Disassociate items before deleting
        List<Item> items = itemRepository.findByLorryInvoice_Id(lr.getId());
        for (Item item : items) {
            item.setLorryInvoice(null);
            itemRepository.save(item);
        }
        lorryReceiptRepository.delete(lr);
    }

    // PRIVATE MAPPER METHOD
    private LorryReceiptResponseDto mapToDto(LorryInvoice lr) {
        List<Item> items = itemRepository.findByLorryInvoice_Id(lr.getId());
        List<ItemResponseDto> itemsDto = items.stream().map(this::mapItemToDto).collect(Collectors.toList());

        return LorryReceiptResponseDto.builder()
                .id(lr.getId())
                .wayBillNo(lr.getWayBillNo())
                .date(lr.getDate())
                .items(itemsDto)
                .collType(lr.getCollType())
                .delvType(lr.getDelvType())
                .gcType(lr.getGcType())
                .modeOfPayment(lr.getModeOfPayment() != null ? lr.getModeOfPayment().name() : null)
                .freightPayBy(lr.getFreightPayBy())
                .actualWeight(lr.getActualWeight())
                .ratePerKg(lr.getRatePerKg())
                .totalPackages(lr.getTotalPackages())
                .invoiceValue(lr.getInvoiceValue())
                .subTotal(lr.getSubTotal())
                .igstTax(lr.getIgstTax())
                .cgstTax(lr.getCgstTax())
                .sgstTax(lr.getSgstTax())
                .grandTotal(lr.getGrandTotal())
                .status(lr.getStatus() != null ? lr.getStatus().name() : null)
                .build();
    }

    private ItemResponseDto mapItemToDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .partNumber(item.getPartNumber())
                .itemQuantity(item.getItemQuantity())
                .itemDescription(item.getItemDescription())
                .quantityInBox(item.getQuantityInBox())
                .weightPerQuantity(item.getWeightPerQuantity())
                .rateOnBox(item.getRateOnBox())
                .rateOnWeight(item.getRateOnWeight())
                .route(item.getRoute() != null ? mapRouteToDto(item.getRoute()) : null)
                .build();
    }

    private RouteResponseDto mapRouteToDto(Route route) {
        return RouteResponseDto.builder()
                .id(route.getId())
                .routeNumber(route.getRouteNumber())
                .routeName(route.getRouteName())
                .startLocation(route.getStartLocation())
                .endLocation(route.getEndLocation())
                .type(route.getType())
                .consignerId(route.getConsignerId() != null ? mapPartyToDto(route.getConsignerId()) : null)
                .consigneeId(route.getConsigneeId() != null ? mapPartyToDto(route.getConsigneeId()) : null)
                .vehicle(route.getVehicle() != null ? mapVehicleToDto(route.getVehicle()) : null)
                .build();
    }

    private PartyResponseDto mapPartyToDto(Party party) {
        return PartyResponseDto.builder()
                .id(party.getId())
                .partyCode(party.getPartyCode())
                .partyName(party.getPartyName())
                .partyNumber(party.getPartyNumber())
                .partyAddress(party.getPartyAddress())
                .gstNumber(party.getGstNumber())
                .district(party.getDistrict())
                .pinCode(party.getPinCode())
                .stateCode(party.getStateCode())
                .vendorMailId(party.getVendorMailId())
                .division(party.getDivision())
                .branchDto(party.getBranch() != null ? mapBranchToDto(party.getBranch()) : null)
                .build();
    }

    private BranchResponceDto mapBranchToDto(Branch branch) {
        return BranchResponceDto.builder()
                .branchId(branch.getId())
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .build();
    }

    private VehicleResponseDto mapVehicleToDto(Vehicle vehicle) {
        return VehicleResponseDto.builder()
                .vehicleId(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleCode(vehicle.getVehicleCode())
                .driverName(vehicle.getDriverName())
                .driverLicenceNo(vehicle.getDriverLicenceNo())
                .driverPermitNo(vehicle.getDriverPermitNo())
                .model(vehicle.getModel())
                .capacity(vehicle.getCapacity())
                .vehicleExpNo(vehicle.getVehicleExpNo())
                .owner(vehicle.getOwner() != null ? mapOwnerToDto(vehicle.getOwner()) : null)
                .branch(vehicle.getBranch() != null ? mapBranchToDto(vehicle.getBranch()) : null)
                .type(vehicle.getType())
                .status(vehicle.getStatus())
                .build();
    }

    private OwnerResponceDtos mapOwnerToDto(Owner owner) {
        return new OwnerResponceDtos(owner.getId(), owner.getOwnerName(), owner.getContactNumber(), owner.getAddress(),
                owner.getEmail());
    }

    @Override
    public List<LorryInvoiceDetailsResponse> getLorryInvoiceDetails(
            UUID consignerId, UUID consigneeId) {
        return lorryReceiptRepository.findLorryInvoiceDetailsByConsignerAndConsignee(consignerId, consigneeId);
    }

    //==========Mail=====================
    private final EmailService emailService;
    @Override
    public void sendInvoiceWithPdf(UUID id) {

        LorryReceiptResponseDto dto = this.getLorryReceiptById(id);

        byte[] pdf = PdfGenerator.generateLorryInvoicePdf(dto);

        List<String> recipients = new ArrayList<>();
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            var item = dto.getItems().get(0);
            if (item.getRoute().getConsignerId() != null &&
                    item.getRoute().getConsignerId().getVendorMailId() != null &&
                    !item.getRoute().getConsignerId().getVendorMailId().isEmpty()) {
                recipients.add(item.getRoute().getConsignerId().getVendorMailId());
            }
            if (item.getRoute().getConsigneeId() != null &&
                    item.getRoute().getConsigneeId().getVendorMailId() != null &&
                    !item.getRoute().getConsigneeId().getVendorMailId().isEmpty()) {
                recipients.add(item.getRoute().getConsigneeId().getVendorMailId());
            }
        }

        if (recipients.isEmpty()) {
            throw new IllegalArgumentException("No email addresses found for consigner or consignee.");
        }

        String emailBody = EmailTemplateUtil.generateInvoiceEmailBody(dto);

        emailService.sendInvoiceEmail(
                recipients,
                "Lorry Invoice - " + dto.getWayBillNo(),
                emailBody,
                pdf,
                "lorry_invoice_" + dto.getWayBillNo() + ".pdf"
        );
        log.info("Invoice email sent for WayBill No: {}", dto.getWayBillNo());
    }
}
