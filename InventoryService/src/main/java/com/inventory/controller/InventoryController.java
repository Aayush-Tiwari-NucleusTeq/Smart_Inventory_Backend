package com.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.inventory.in.dto.InventoryInDto;
import com.inventory.out.dto.InventoryOutDto;
import com.inventory.services.InventoryService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private PubSubTemplate pubSubTemplate;

	@PostMapping
	public ResponseEntity<InventoryOutDto> saveInventory(@Valid @RequestBody InventoryInDto inventoryInDto){
		log.info("Saving new inventory");
		return ResponseEntity.ok(this.inventoryService.saveInventory(inventoryInDto));
	}
	
	@PutMapping
	public ResponseEntity<InventoryOutDto> updateInventory(@RequestParam String productID, @RequestParam int stock){
		log.info("Updating inventory: productID={}, stock={}", productID, stock);
		InventoryOutDto updateInventory = this.inventoryService.updateInventory(productID, stock);
		this.pubSubTemplate.publish("order-topic", updateInventory.toString());
		return ResponseEntity.ok(updateInventory);
	}
	
	@PutMapping("/{productID}/{stock}")
	public ResponseEntity<InventoryOutDto> updateInventoryFromOrderService(@PathVariable("productID") String productID,@PathVariable("stock") int stock){
		InventoryOutDto updateInventory = this.inventoryService.updateInventory(productID, stock);
		return ResponseEntity.ok(updateInventory);
	}
	
	@GetMapping("/{productID}")
	public ResponseEntity<InventoryOutDto> getInventory(@PathVariable("productID") String productID){
		 log.info("Fetching inventory for productID={}", productID);
		return ResponseEntity.ok(this.inventoryService.getInventory(productID));
	}
	
	@GetMapping
	public ResponseEntity<List<InventoryOutDto>> getAllInventories(){
		log.info("Fetching all inventories");
		return ResponseEntity.ok(this.inventoryService.getAllInventories());
	}
	
	@DeleteMapping("/{productID}")
	public ResponseEntity<String> deleteInventory(@PathVariable("productID") String productID){
		log.info("Deleting inventory for productID={}", productID);
		return ResponseEntity.ok(this.inventoryService.deleteInventory(productID));
	}
	
	@PostMapping("/placeorder")
    public ResponseEntity<String> placeOrder(@RequestBody String message) {
		log.info("Placing order with message: {}", message);
        pubSubTemplate.publish("order-topic", message);
        return ResponseEntity.ok("Inventory updated!");
    }
}
