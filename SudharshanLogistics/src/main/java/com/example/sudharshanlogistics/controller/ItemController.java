package com.example.sudharshanlogistics.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.dtos.item.ItemRequestDto;
import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;
import com.example.sudharshanlogistics.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@Tag(name = "Item Controller", description = "CRUD operations for Item entity")
@CrossOrigin("*")
public class ItemController {

  private final ItemService itemService;

  @PostMapping("/create-item")
  @Operation(summary = "Create Item", description = "Create a new item")
  public ResponseEntity<?> createItem(@RequestBody ItemRequestDto itemRequestDto) {
    try {
      ItemResponseDto responseDto = itemService.createItem(itemRequestDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This part number is already in use");
    }
  }

  @GetMapping("/get-item")
  @Operation(summary = "Get Item by ID", description = "Get item details by ID")
  public ResponseEntity<?> getItemById(@RequestParam UUID id) {
    try {
      ItemResponseDto responseDto = itemService.getItemById(id);
      return ResponseEntity.ok(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/get-all-item")
  @Operation(summary = "Get All Items", description = "Get list of all items")
  public ResponseEntity<?> getAllItems(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "partNumber") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<ItemResponseDto> items = itemService.getAllItems(pageable);
    return ResponseEntity.ok(items);
  }

  @GetMapping("search-item")
  @Operation(summary = "search All Items", description = "Get list of all searching items")
  public ResponseEntity<?> getSearchItem(@RequestParam(required = false) String itemName) {
    List<ItemResponseDto> items = itemService.searchItems(itemName);
    return items.isEmpty()
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are searching with this code or name is not found")
        : ResponseEntity.ok(items);
  }

  @PutMapping("/update-item")
  @Operation(summary = "Update Item", description = "Update item details by ID")
  public ResponseEntity<?> updateItem(@RequestParam UUID id, @RequestBody ItemRequestDto itemRequestDto) {
    try {
      ItemResponseDto responseDto = itemService.updateItem(id, itemRequestDto);
      return ResponseEntity.ok(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This part number is already in use");
    }
  }

  @DeleteMapping("/delete-item")
  @Operation(summary = "Delete Item", description = "Delete item by ID")
  public ResponseEntity<?> deleteItem(@RequestParam UUID id) {
    try {
      itemService.deleteItem(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/get-item-name")
  @Operation(summary = "Get Item Name by ID", description = "Get item name by ID")
  public ResponseEntity<?> getItemNameById(@RequestParam UUID id) {
    try {
      String itemName = itemService.getItemNameById(id);
      return ResponseEntity.ok(itemName);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }
  }
}
