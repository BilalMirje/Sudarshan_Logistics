package com.example.sudharshanlogistics.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.sudharshanlogistics.entity.dtos.item.ItemRequestDto;
import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;

public interface ItemService {

  ItemResponseDto createItem(ItemRequestDto itemRequestDto);

  ItemResponseDto getItemById(UUID id);

  Page<ItemResponseDto> getAllItems(Pageable pageable);

  List<ItemResponseDto> searchItems(String itemName);

  ItemResponseDto updateItem(UUID id, ItemRequestDto itemRequestDto);

  void deleteItem(UUID id);

  String getItemNameById(UUID id);
}
