package com.order.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.order.entities.Inventory;

//@FeignClient(url = "http://localhost:9092", value = "Inventory-Client")
@FeignClient(name = "InventoryService")
public interface InventoryClient {

	@GetMapping("/inventory/{productId}")
	Inventory getInventories(@PathVariable String productId);
}
