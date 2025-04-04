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
import com.inventory.in.dto.InventoryInDto;
import com.inventory.out.dto.InventoryOutDto;
import com.inventory.services.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;

	@PostMapping
	public ResponseEntity<InventoryOutDto> saveInventory(@Valid @RequestBody InventoryInDto inventoryInDto){
		return ResponseEntity.ok(this.inventoryService.saveInventory(inventoryInDto));
	}
	
	@PutMapping
	public ResponseEntity<InventoryOutDto> updateInventory(@RequestParam String productID, @RequestParam int stock){
		return ResponseEntity.ok(this.inventoryService.updateInventory(productID, stock));
	}
	
	@GetMapping("/{productID}")
	public ResponseEntity<InventoryOutDto> getInventory(@PathVariable("productID") String productID){
		return ResponseEntity.ok(this.inventoryService.getInventory(productID));
	}
	
	@GetMapping
	public ResponseEntity<List<InventoryOutDto>> getAllInventories(){
		return ResponseEntity.ok(this.inventoryService.getAllInventories());
	}
	
	@DeleteMapping("/{productID}")
	public ResponseEntity<String> deleteInventory(@PathVariable("productID") String productID){
		return ResponseEntity.ok(this.inventoryService.deleteInventory(productID));
	}
}
