package com.example.sudharshanlogistics.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.LrRecepit;

@Repository
public interface LrInvoiceRepository extends JpaRepository<LrRecepit, UUID> {

  boolean existsByInvoiceNo(String invoiceNo);

  @Query("select count(e) > 0 from LrRecepit e where e.cNoteNo = ?1")
  boolean existsByCNoteNo(String cNoteNo);

  @Query("select max(e.cNoteNo) from LrRecepit e")
  String findMaxCNoteNo();

  @Modifying(clearAutomatically = true)
  @Query("UPDATE LrRecepit l SET l.date = :date, l.paymentMode = :paymentMode, l.insured = :insured, l.doorDeliveryCharge = :doorDeliveryCharge, l.doorPickupCharge = :doorPickupCharge, l.hamaliCharge = :hamaliCharge, l.statisticalCharge = :statisticalCharge, l.serviceCharge = :serviceCharge, l.totalFreight = :totalFreight, l.invoiceNo = :invoiceNo, l.invoiceValue = :invoiceValue, l.amountInWords = :amountInWords WHERE l.id = :id")
  int updateLrInvoiceFields(UUID id, LocalDate date, LrRecepit.PaymentMode paymentMode, boolean insured,
      double doorDeliveryCharge, double doorPickupCharge, double hamaliCharge, double statisticalCharge,
      double serviceCharge, double totalFreight, String invoiceNo, double invoiceValue, String amountInWords);

  List<LrRecepit> findByBranch_BranchCodeAndDateBetween(String branchCode, LocalDate startDate, LocalDate endDate);

  @Query("SELECT new com.example.sudharshanlogistics.entity.dtos.RouteItemVehicleResponse(r.startLocation, r.endLocation, i.itemQuantity, i.itemDescription, v.vehicleNumber) "
      +
      "FROM LrRecepit lr JOIN lr.items i JOIN i.route r JOIN r.vehicle v " +
      "WHERE r.consignerId.id = :consignerId AND r.consigneeId.id = :consigneeId")
  List<com.example.sudharshanlogistics.entity.dtos.RouteItemVehicleResponse> findRouteItemVehicleDetailsByConsignerAndConsignee(
      UUID consignerId, UUID consigneeId);

  // No need for custom queries since itemNames is now @ElementCollection
}
