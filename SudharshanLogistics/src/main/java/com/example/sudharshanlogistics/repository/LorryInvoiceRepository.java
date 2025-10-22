package com.example.sudharshanlogistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.LorryInvoice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LorryInvoiceRepository extends JpaRepository<LorryInvoice, UUID> {
        Optional<LorryInvoice> findByWayBillNo(String wayBillNo);

        @Query("SELECT new com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryInvoiceDetailsResponse(" +
                        "consigner.gstNumber, consignee.gstNumber, r.startLocation, r.endLocation, " +
                        "i.itemName, i.itemDescription, i.itemQuantity, v.vehicleNumber) " +
                        "FROM Item i " +
                        "JOIN i.route r " +
                        "JOIN r.consignerId consigner " +
                        "JOIN r.consigneeId consignee " +
                        "JOIN r.vehicle v " +
                        "WHERE r.consignerId.id = :consignerId AND r.consigneeId.id = :consigneeId")
        List<com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryInvoiceDetailsResponse> findLorryInvoiceDetailsByConsignerAndConsignee(
                        UUID consignerId, UUID consigneeId);
}
