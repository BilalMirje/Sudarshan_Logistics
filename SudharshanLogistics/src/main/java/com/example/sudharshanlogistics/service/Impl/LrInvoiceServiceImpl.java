package com.example.sudharshanlogistics.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.sudharshanlogistics.entity.LrRecepit;
import com.example.sudharshanlogistics.entity.dtos.lrInvoice.LrInvoiceRequestDto;
import com.example.sudharshanlogistics.entity.dtos.lrInvoice.LrInvoiceResponseDto;
import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;
import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;
import com.example.sudharshanlogistics.entity.dtos.RouteItemVehicleResponse;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.entity.Item;
import com.example.sudharshanlogistics.entity.Route;
import com.example.sudharshanlogistics.entity.Party;
import com.example.sudharshanlogistics.entity.Vehicle;
import com.example.sudharshanlogistics.entity.Owner;
import com.example.sudharshanlogistics.entity.Branch;
import com.example.sudharshanlogistics.repository.AccountRepository;
import com.example.sudharshanlogistics.repository.BranchRepository;
import com.example.sudharshanlogistics.repository.ItemRepository;
import com.example.sudharshanlogistics.repository.LrInvoiceRepository;
import com.example.sudharshanlogistics.repository.PartyRepository;
import com.example.sudharshanlogistics.repository.RouteRepository;
import com.example.sudharshanlogistics.repository.VehicleRepository;
import com.example.sudharshanlogistics.service.LrRecipitService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LrInvoiceServiceImpl implements LrRecipitService {

  private final LrInvoiceRepository lrInvoiceRepository;
  private final VehicleRepository vehicleRepository;
  private final RouteRepository routeRepository;
  private final PartyRepository partyRepository;
  private final ItemRepository itemRepository;
  private final BranchRepository branchRepository;
  private final AccountRepository accountRepository;

  @PersistenceContext
  private EntityManager entityManager;

  private ItemResponseDto mapItemToDto(Item item) {
    if (item == null)
      return null;
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
        .route(mapRouteToDto(item.getRoute()))
        .build();
  }

  private RouteResponseDto mapRouteToDto(Route route) {
    if (route == null)
      return null;
    return com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto.builder()
        .id(route.getId())
        .routeNumber(route.getRouteNumber())
        .routeName(route.getRouteName())
        .startLocation(route.getStartLocation())
        .endLocation(route.getEndLocation())
        .type(route.getType())
        .consignerId(mapPartyToDto(route.getConsignerId()))
        .consigneeId(mapPartyToDto(route.getConsigneeId()))
        .vehicle(mapVehicleToDto(route.getVehicle()))
        .build();
  }

  private BranchResponceDto mapBranchToDto(Branch branch) {
    if (branch == null)
      return null;
    return BranchResponceDto.builder()
        .branchId(branch.getId())
        .branchCode(branch.getBranchCode())
        .branchName(branch.getBranchName())
        .build();
  }

  private PartyResponseDto mapPartyToDto(Party party) {
    if (party == null)
      return null;
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
        .branchDto(mapBranchToDto(party.getBranch()))
        .build();
  }

  private OwnerResponceDtos mapOwnerToDto(Owner owner) {
    if (owner == null)
      return null;
    return new OwnerResponceDtos(
        owner.getId(),
        owner.getOwnerName(),
        owner.getContactNumber(),
        owner.getAddress(),
        owner.getEmail());
  }

  private VehicleResponseDto mapVehicleToDto(Vehicle vehicle) {
    if (vehicle == null)
      return null;
    return VehicleResponseDto.builder()
        .vehicleId(vehicle.getId())
        .vehicleNumber(vehicle.getVehicleNumber())
        .model(vehicle.getModel())
        .capacity(vehicle.getCapacity())
        .status(vehicle.getStatus())
        .owner(mapOwnerToDto(vehicle.getOwner()))
        .branch(mapBranchToDto(vehicle.getBranch()))
        .vehicleCode(vehicle.getVehicleCode())
        .driverName(vehicle.getDriverName())
        .driverLicenceNo(vehicle.getDriverLicenceNo())
        .driverPermitNo(vehicle.getDriverPermitNo())
        .vehicleExpNo(vehicle.getVehicleExpNo())
        .type(vehicle.getType())
        .build();
  }

  @Override
  public LrInvoiceResponseDto createLrInvoice(LrInvoiceRequestDto dto) {
    if (lrInvoiceRepository.existsByInvoiceNo(dto.getInvoiceNo())) {
      throw new RuntimeException("Invoice No already exists");
    }

    Party party = null;
    if (dto.getPaymentMode() == LrRecepit.PaymentMode.CREDIT) {
      if (dto.getPartyId() == null) {
        throw new RuntimeException("Party is required for CREDIT payment mode");
      }
      party = partyRepository.findById(dto.getPartyId())
          .orElseThrow(() -> new RuntimeException("Party not found with id: " + dto.getPartyId()));
      if (!accountRepository.existsByParty(party)) {
        throw new RuntimeException("Party does not have an account. Please create an account first.");
      }
    }

    Party consignee = null;
    if (dto.getConsigneeId() != null) {
      consignee = partyRepository.findById(dto.getConsigneeId())
          .orElseThrow(() -> new RuntimeException("Consignee not found with id: " + dto.getConsigneeId()));
    }

    List<Item> items = new ArrayList<>();
    if (dto.getItemId() != null && !dto.getItemId().isEmpty()) {
      items = itemRepository.findAllById(dto.getItemId());
      if (items.size() != dto.getItemId().size()) {
        throw new RuntimeException("Some items not found");
      }
      items = new ArrayList<>(new LinkedHashSet<>(items));
    }

    double totalFreight = dto.getDoorDeliveryCharge() + dto.getDoorPickupCharge() + dto.getHamaliCharge()
        + dto.getStatisticalCharge() + dto.getServiceCharge();

    LrRecepit lrInvoice = LrRecepit.builder()
        .cNoteNo(generateCNoteNo())
        .date(dto.getDate())
        .paymentMode(dto.getPaymentMode())
        .insured(dto.isInsured())
        .doorDeliveryCharge(dto.getDoorDeliveryCharge())
        .doorPickupCharge(dto.getDoorPickupCharge())
        .hamaliCharge(dto.getHamaliCharge())
        .statisticalCharge(dto.getStatisticalCharge())
        .serviceCharge(dto.getServiceCharge())
        .totalFreight(totalFreight)
        .invoiceNo(dto.getInvoiceNo())
        .invoiceValue(dto.getInvoiceValue())
        .amountInWords(dto.getAmountInWords())
        .transportId(generateTransportId(dto.getDate()))
        .party(party)
        .consignee(consignee)
        .build();

    LrRecepit savedLrInvoice = lrInvoiceRepository.save(lrInvoice);

    for (Item item : items) {
      if (item.getLrInvoice() != null) {
        LrRecepit oldInvoice = lrInvoiceRepository.findById(item.getLrInvoice().getId()).orElse(null);
        if (oldInvoice != null) {
          oldInvoice.getItems().remove(item);
          lrInvoiceRepository.save(oldInvoice);
        }
      }
      item.setLrInvoice(savedLrInvoice);
      itemRepository.save(item);
    }
    return mapToResponseDto(savedLrInvoice);
  }

  @Override
  public LrInvoiceResponseDto getLrInvoice(UUID id) {
    LrRecepit lrInvoice = lrInvoiceRepository.findById(id).orElse(null);
    if (lrInvoice != null) {
      return mapToResponseDto(lrInvoice);
    }
    return null;
  }

  @Override
  public Page<LrInvoiceResponseDto> getAllLrInvoices(Pageable pageable) {
    Page<LrRecepit> lrInvoices = lrInvoiceRepository.findAll(pageable);
    return lrInvoices.map(this::mapToResponseDto);
  }

  @Override
  @Transactional
  public LrInvoiceResponseDto updateLrInvoice(UUID id, LrInvoiceRequestDto dto) {
    Optional<LrRecepit> optional = lrInvoiceRepository.findById(id);
    if (optional.isEmpty()) {
      return null;
    }
    LrRecepit lrInvoice = optional.get();

    if (!lrInvoice.getInvoiceNo().equals(dto.getInvoiceNo())
        && lrInvoiceRepository.existsByInvoiceNo(dto.getInvoiceNo())) {
      throw new RuntimeException("Invoice No already exists");
    }

    List<Item> dbItems = dto.getItemId() != null && !dto.getItemId().isEmpty()
        ? itemRepository.findAllById(dto.getItemId())
        : new ArrayList<>();

    if (dbItems.size() != dto.getItemId().size()) {
      throw new RuntimeException("Some items not found");
    }

    List<Item> items = new ArrayList<>(new LinkedHashSet<>(dbItems));

    double totalFreight = dto.getDoorDeliveryCharge() + dto.getDoorPickupCharge() + dto.getHamaliCharge()
        + dto.getStatisticalCharge() + dto.getServiceCharge();

    // Update LrInvoice fields using custom query to avoid auditing issues
    lrInvoiceRepository.updateLrInvoiceFields(id, dto.getDate(), dto.getPaymentMode(), dto.isInsured(),
        dto.getDoorDeliveryCharge(), dto.getDoorPickupCharge(), dto.getHamaliCharge(),
        dto.getStatisticalCharge(), dto.getServiceCharge(), totalFreight, dto.getInvoiceNo(),
        dto.getInvoiceValue(), dto.getAmountInWords());

    // Reload the updated entity
    LrRecepit saved = lrInvoiceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("LrInvoice not found after update"));

    // Set the party if payment mode is CREDIT
    Party party = null;
    if (dto.getPaymentMode() == LrRecepit.PaymentMode.CREDIT) {
      if (dto.getPartyId() == null) {
        throw new RuntimeException("Party is required for CREDIT payment mode");
      }
      party = partyRepository.findById(dto.getPartyId())
          .orElseThrow(() -> new RuntimeException("Party not found with id: " + dto.getPartyId()));
      if (!accountRepository.existsByParty(party)) {
        throw new RuntimeException("Party does not have an account. Please create an account first.");
      }
    }

    Party consignee = null;
    if (dto.getConsigneeId() != null) {
      consignee = partyRepository.findById(dto.getConsigneeId())
          .orElseThrow(() -> new RuntimeException("Consignee not found with id: " + dto.getConsigneeId()));
    }

    saved.setParty(party);
    saved.setConsignee(consignee);
    lrInvoiceRepository.save(saved);

    if (dto.getItemId() != null) {
      for (Item item : items) {
        if (item.getLrInvoice() != null && !item.getLrInvoice().getId().equals(saved.getId())) {
          LrRecepit oldInvoice = lrInvoiceRepository.findById(item.getLrInvoice().getId()).orElse(null);
          if (oldInvoice != null) {
            oldInvoice.getItems().remove(item);
            lrInvoiceRepository.save(oldInvoice); // Save the old invoice after removing the item
          }
        }
        item.setLrInvoice(saved);
        itemRepository.save(item);
      }
    }

    // Flush to ensure all changes are persisted
    entityManager.flush();

    // Clear the persistence context to ensure queries see the updated database
    // state
    entityManager.clear();

    // Reload the updated entity to ensure items are loaded
    LrRecepit updatedLrInvoice = lrInvoiceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("LrInvoice not found after item update"));

    return mapToResponseDto(updatedLrInvoice);
  }

  @Override
  public boolean deleteLrInvoice(UUID id) {
    Optional<LrRecepit> optional = lrInvoiceRepository.findById(id);
    if (optional.isPresent()) {
      LrRecepit lrInvoice = optional.get();
      List<Item> items = itemRepository.findByLrInvoice(lrInvoice);
      if (items != null) {
        for (Item item : items) {
          item.setLrInvoice(null);
          itemRepository.save(item);
        }
      }
      lrInvoiceRepository.delete(lrInvoice);
      return true;
    }
    return false;
  }

  @Override
  public List<LrInvoiceResponseDto> getLrReceiptsByBranchAndDate(String branchCode, LocalDate startDate,
      LocalDate endDate) {
    Optional<Branch> branchOpt = branchRepository.findByBranchCode(branchCode);
    if (branchOpt.isEmpty()) {
      throw new RuntimeException("Branch code not found: " + branchCode);
    }
    List<LrRecepit> lrReceipts = lrInvoiceRepository.findByBranch_BranchCodeAndDateBetween(branchCode, startDate,
        endDate);
    return lrReceipts.stream().map(this::mapToResponseDto).toList();
  }

  private String generateTransportId(LocalDate date) {
    String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    // Find the max transportId for the date
    List<LrRecepit> invoices = lrInvoiceRepository.findAll();
    int maxSeq = 0;
    for (LrRecepit inv : invoices) {
      if (inv.getTransportId() != null && inv.getTransportId().startsWith("TRANS-" + dateStr + "-")) {
        String seqStr = inv.getTransportId().substring(inv.getTransportId().lastIndexOf('-') + 1);
        try {
          int seq = Integer.parseInt(seqStr);
          if (seq > maxSeq) {
            maxSeq = seq;
          }
        } catch (NumberFormatException e) {
          // ignore
        }
      }
    }
    int nextSeq = maxSeq + 1;
    return String.format("TRANS-%s-%04d", dateStr, nextSeq);
  }

  private String generateCNoteNo() {
    String maxCNoteNo = lrInvoiceRepository.findMaxCNoteNo();
    int maxSeq = 0;
    if (maxCNoteNo != null) {
      try {
        maxSeq = Integer.parseInt(maxCNoteNo);
      } catch (NumberFormatException e) {
        maxSeq = 0;
      }
    }
    int nextSeq = maxSeq + 1;
    String cNoteNo = String.format("%04d", nextSeq);
    // Ensure uniqueness
    while (lrInvoiceRepository.existsByCNoteNo(cNoteNo)) {
      nextSeq++;
      cNoteNo = String.format("%04d", nextSeq);
    }
    return cNoteNo;
  }

  private LrInvoiceResponseDto mapToResponseDto(LrRecepit lrInvoice) {
    List<Item> items = itemRepository.findByLrInvoiceId(lrInvoice.getId());
    List<ItemResponseDto> itemDtos = items != null ? items.stream().map(this::mapItemToDto).toList() : null;
    return LrInvoiceResponseDto.builder()
        .id(lrInvoice.getId())
        .cNoteNo(lrInvoice.getCNoteNo())
        .date(lrInvoice.getDate())
        .paymentMode(lrInvoice.getPaymentMode())
        .insured(lrInvoice.isInsured())
        .items(itemDtos)
        .party(mapPartyToDto(lrInvoice.getParty()))
        .consignee(mapPartyToDto(lrInvoice.getConsignee()))
        .doorDeliveryCharge(lrInvoice.getDoorDeliveryCharge())
        .doorPickupCharge(lrInvoice.getDoorPickupCharge())
        .hamaliCharge(lrInvoice.getHamaliCharge())
        .statisticalCharge(lrInvoice.getStatisticalCharge())
        .serviceCharge(lrInvoice.getServiceCharge())
        .totalFreight(lrInvoice.getTotalFreight())
        .invoiceNo(lrInvoice.getInvoiceNo())
        .invoiceValue(lrInvoice.getInvoiceValue())
        .amountInWords(lrInvoice.getAmountInWords())
        .transportId(lrInvoice.getTransportId())
        .build();
  }

  @Override
  public String generateInvoiceDetails(UUID lrId) {
    LrRecepit lrInvoice = lrInvoiceRepository.findById(lrId)
        .orElseThrow(() -> new RuntimeException("LR Invoice not found"));
    StringBuilder details = new StringBuilder();
    details.append("Invoice No: ").append(lrInvoice.getInvoiceNo()).append("\n");
    details.append("Date: ").append(lrInvoice.getDate()).append("\n");
    details.append("C.Note No: ").append(lrInvoice.getCNoteNo()).append("\n");
    details.append("Transport ID: ").append(lrInvoice.getTransportId()).append("\n");
    if (lrInvoice.getParty() != null) {
      details.append("Party: ").append(lrInvoice.getParty().getPartyName()).append("\n");
    }
    if (lrInvoice.getConsignee() != null) {
      details.append("Consignee: ").append(lrInvoice.getConsignee().getPartyName()).append("\n");
    }
    details.append("Payment Mode: ").append(lrInvoice.getPaymentMode()).append("\n");
    details.append("Insured: ").append(lrInvoice.isInsured()).append("\n");
    details.append("Invoice Value: ").append(lrInvoice.getInvoiceValue()).append("\n");
    details.append("Amount in Words: ").append(lrInvoice.getAmountInWords()).append("\n");
    details.append("Total Freight: ").append(lrInvoice.getTotalFreight()).append("\n");
    return details.toString();
  }

  @Override
  public String generateReceiptDetails(UUID lrId) {
    LrRecepit lrInvoice = lrInvoiceRepository.findById(lrId)
        .orElseThrow(() -> new RuntimeException("LR Invoice not found"));
    StringBuilder details = new StringBuilder();
    details.append("Receipt for Invoice No: ").append(lrInvoice.getInvoiceNo()).append("\n");
    details.append("Date: ").append(lrInvoice.getDate()).append("\n");
    details.append("C.Note No: ").append(lrInvoice.getCNoteNo()).append("\n");
    details.append("Transport ID: ").append(lrInvoice.getTransportId()).append("\n");
    if (lrInvoice.getParty() != null) {
      details.append("Party: ").append(lrInvoice.getParty().getPartyName()).append("\n");
    }
    if (lrInvoice.getConsignee() != null) {
      details.append("Consignee: ").append(lrInvoice.getConsignee().getPartyName()).append("\n");
    }
    details.append("Payment Mode: ").append(lrInvoice.getPaymentMode()).append("\n");
    details.append("Total Freight: ").append(lrInvoice.getTotalFreight()).append("\n");
    details.append("Amount Paid: ").append(lrInvoice.getTotalFreight()).append("\n"); // Assuming full payment
    return details.toString();
  }

  @Override
  public List<RouteItemVehicleResponse> getRouteItemVehicleDetails(
      UUID consignerId, UUID consigneeId) {
    return lrInvoiceRepository.findRouteItemVehicleDetailsByConsignerAndConsignee(consignerId, consigneeId);
  }
}
