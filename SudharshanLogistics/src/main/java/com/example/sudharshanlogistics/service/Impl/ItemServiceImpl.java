package com.example.sudharshanlogistics.service.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudharshanlogistics.entity.Item;
import com.example.sudharshanlogistics.entity.Route;
import com.example.sudharshanlogistics.entity.dtos.item.ItemRequestDto;
import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;
import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;
import com.example.sudharshanlogistics.repository.ItemRepository;
import com.example.sudharshanlogistics.repository.RouteRepository;
import com.example.sudharshanlogistics.service.ItemService;
import com.example.sudharshanlogistics.service.RouteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;
  private final RouteRepository routeRepository;
  private final RouteService routeService;

  private RouteResponseDto mapRouteToDto(Route route) {
    return routeService.mapToDto(route);
  }

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

  private Item mapToEntity(ItemRequestDto dto) {
    Route route = routeRepository.findById(dto.getRouteId())
        .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + dto.getRouteId()));
    return Item.builder()
        .itemName(dto.getItemName())
        .partNumber(dto.getPartNumber())
        .quantityInBox(dto.getQuantityInBox())
        .weightPerQuantity(dto.getWeightPerQuantity())
        .rateOnBox(dto.getRateOnBox())
        .rateOnWeight(dto.getRateOnWeight())
        .itemQuantity(dto.getItemQuantity())
        .itemDescription(dto.getItemDescription())
        .route(route)
        .build();
  }

  @Override
  public ItemResponseDto createItem(ItemRequestDto itemRequestDto) {
    if (itemRepository.existsByPartNumber(itemRequestDto.getPartNumber()))
      throw new RuntimeException("This part number is already in use!");
    Item item = mapToEntity(itemRequestDto);
    return mapItemToDto(itemRepository.save(item));
  }

  @Override
  public ItemResponseDto getItemById(UUID id) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    return mapItemToDto(item);
  }

  @Override
  public Page<ItemResponseDto> getAllItems(Pageable pageable) {
    Page<Item> items = itemRepository.findAll(pageable);
    return items
        .map(this::mapItemToDto);
  }

  @Override
  public ItemResponseDto updateItem(UUID id, ItemRequestDto itemRequestDto) {
    Item existingItem = itemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));

    Route route = routeRepository.findById(itemRequestDto.getRouteId())
        .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + itemRequestDto.getRouteId()));

    if (!existingItem.getPartNumber().equals(itemRequestDto.getPartNumber())
        && itemRepository.existsByPartNumber(itemRequestDto.getPartNumber()))
      throw new RuntimeException("This part number is already in use!");
    existingItem.setItemName(itemRequestDto.getItemName());
    existingItem.setPartNumber(itemRequestDto.getPartNumber());
    existingItem.setQuantityInBox(itemRequestDto.getQuantityInBox());
    existingItem.setWeightPerQuantity(itemRequestDto.getWeightPerQuantity());
    existingItem.setRateOnBox(itemRequestDto.getRateOnBox());
    existingItem.setRateOnWeight(itemRequestDto.getRateOnWeight());
    existingItem.setRoute(route);

    Item updatedItem = itemRepository.save(existingItem);
    return mapItemToDto(updatedItem);
  }

  @Override
  public void deleteItem(UUID id) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    itemRepository.delete(item);
  }

  @Override
  public List<ItemResponseDto> searchItems(String itemName) {
    List<Item> items = itemRepository
        .findByItemNameContainingIgnoreCase(
            itemName);
    return items.stream().map(this::mapItemToDto).toList();
  }

  @Override
  public String getItemNameById(UUID id) {
    return itemRepository.findItemNameById(id);
  }
}
