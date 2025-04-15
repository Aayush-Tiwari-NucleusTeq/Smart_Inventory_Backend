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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	public InventoryOutDto saveInventory(InventoryInDto inventoryInDto) {
		Inventory inventory = this.inventoryInDtoToInventory(inventoryInDto);
		String randomProductId = "PROD_" + UUID.randomUUID().toString().substring(0,15);
		inventory.setProductId(randomProductId);
		log.info("Saving inventory for product -> {} with stock -> {}", inventory.getProductId(), inventory.getStock());
		Inventory savedInventory =  this.inventoryRepository.save(inventory);
		log.info("Inventory saved with the product ID -> {}", inventory.getProductId());
		InventoryOutDto inventoryOutDto = this.inventoryToInventoryOutDto(savedInventory);
		return inventoryOutDto;
	}

	@Override
	public InventoryOutDto updateInventory(String productId, int stock) {
		log.info("Updating inventory for product ID -> {} with stock -> {}", productId, stock);
		Inventory inventory = this.inventoryRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("The product with product ID - [" + productId + "] is not present !! Please check the product ID"));
		inventory.setStock(stock);
		Inventory updatedInventory = this.inventoryRepository.save(inventory);
		log.info("Updated inventory for product ID -> {}", productId);
		InventoryOutDto inventoryOutDto = this.inventoryToInventoryOutDto(updatedInventory);
		return inventoryOutDto;
	}

	@Override
	public List<InventoryOutDto> getAllInventories() {
		log.info("Fetching all inventories");
		List<Inventory> inventories = this.inventoryRepository.findAll();
		List<InventoryOutDto> inventoryOutDtos = inventories.stream().map(inventory -> this.inventoryToInventoryOutDto(inventory)).collect(Collectors.toList());
		return inventoryOutDtos;
	}

	@Override
	public InventoryOutDto getInventory(String product_id) {
		log.info("Fetching inventory for product ID -> {}", product_id);
		Inventory inventory = this.inventoryRepository.findById(product_id).orElseThrow(()->new ResourceNotFoundException("The product with product ID - [" + product_id + "] is not present !! Please check the product ID"));
		log.info("Fetched inventory for product ID -> {}", product_id);
		InventoryOutDto inventoryOutDto = this.inventoryToInventoryOutDto(inventory);
		return inventoryOutDto;
	}

	@Override
	public String deleteInventory(String product_id) {
		log.info("Deleting inventory for product ID -> {}", product_id);
		Inventory inventory = this.inventoryRepository.findById(product_id).orElseThrow(()->new ResourceNotFoundException("The product with product ID - [" + product_id + "] is not present !! Please check the product ID"));
		if(inventory != null) {
			this.inventoryRepository.deleteById(product_id);
			log.info("Deleted inventory for product ID -> {}", product_id);
			return "Inventory is deleted successfully";
		} else {
			System.out.println("Something went wrong");
			log.info("Some error has occured, please check!!");
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
