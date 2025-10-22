package com.example.sudharshanlogistics.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.Item;
import com.example.sudharshanlogistics.entity.LrRecepit;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

  boolean existsByPartNumber(String partNumber);

  @Query("SELECT i FROM Item i WHERE LOWER(i.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))")
  List<Item> findByItemNameContainingIgnoreCase(
      String itemName);

  @Query("SELECT i.itemName FROM Item i WHERE i.id = :id")
  String findItemNameById(UUID id);

  List<Item> findByLrInvoice(LrRecepit lrInvoice);

  List<Item> findByLrInvoiceId(UUID id);

  List<Item> findByLorryInvoice_Id(UUID lorryInvoiceId);

}
