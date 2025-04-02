package com.inventory.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.entities.Inventory;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.in.dto.InventoryInDto;
import com.inventory.out.dto.InventoryOutDto;
import com.inventory.repository.InventoryRepository;
import com.inventory.services.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	public InventoryOutDto saveInventory(InventoryInDto inventoryInDto) {
		Inventory inventory = this.inventoryInDtoToInventory(inventoryInDto);
		String randomProductId = "PROD_" + UUID.randomUUID().toString().substring(0,15);
		inventory.setProductId(randomProductId);
		Inventory savedInventory =  this.inventoryRepository.save(inventory);
		InventoryOutDto inventoryOutDto = this.inventoryToInventoryOutDto(savedInventory);
		return inventoryOutDto;
	}

	@Override
	public InventoryOutDto updateInventory(String productId, int stock) {
		Inventory inventory = this.inventoryRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("The product with product ID - [" + productId + "] is not present !! Please check the product ID"));
		inventory.setStock(stock);
		Inventory updatedInventory = this.inventoryRepository.save(inventory);
		InventoryOutDto inventoryOutDto = this.inventoryToInventoryOutDto(updatedInventory);
		return inventoryOutDto;
	}

	@Override
	public List<InventoryOutDto> getAllInventories() {
		List<Inventory> inventories = this.inventoryRepository.findAll();
		List<InventoryOutDto> inventoryOutDtos = inventories.stream().map(inventory -> this.inventoryToInventoryOutDto(inventory)).collect(Collectors.toList());
		return inventoryOutDtos;
	}

	@Override
	public InventoryOutDto getInventory(String product_id) {
		Inventory inventory = this.inventoryRepository.findById(product_id).orElseThrow(()->new ResourceNotFoundException("The product with product ID - [" + product_id + "] is not present !! Please check the product ID"));
		InventoryOutDto inventoryOutDto = this.inventoryToInventoryOutDto(inventory);
		return inventoryOutDto;
	}

	@Override
	public String deleteInventory(String product_id) {
		Inventory inventory = this.inventoryRepository.findById(product_id).orElseThrow(()->new ResourceNotFoundException("The product with product ID - [" + product_id + "] is not present !! Please check the product ID"));
		if(inventory != null) {
			this.inventoryRepository.deleteById(product_id);
			return "Inventory is deleted successfully";
		} else {
			System.out.println("Something went wrong");
			return "Something went wrong";
		}
	}
	
	public Inventory inventoryInDtoToInventory(InventoryInDto inventoryInDto) {
		Inventory inventory = new Inventory();
		inventory.setStock(inventoryInDto.getStock());
		return inventory;
	}
	
	public InventoryOutDto inventoryToInventoryOutDto(Inventory inventory) {
		InventoryOutDto inventoryOutDto = new InventoryOutDto();
		inventoryOutDto.setProductId(inventory.getProductId());
		inventoryOutDto.setStock(inventory.getStock());
		return inventoryOutDto;
	}

}
