package com.inventory.services;

import java.util.List;

import com.inventory.in.dto.InventoryInDto;
import com.inventory.out.dto.InventoryOutDto;

public interface InventoryService {

	InventoryOutDto saveInventory(InventoryInDto inventory);
	InventoryOutDto updateInventory(String productId, int stock);
	List<InventoryOutDto> getAllInventories();
	InventoryOutDto getInventory(String product_id);
	String deleteInventory(String product_id);
}
