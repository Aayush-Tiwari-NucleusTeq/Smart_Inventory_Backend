package com.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.services.impl.InventoryService;

@RestController
@RequestMapping("/otest")
public class HomeController {
	
	@Autowired
	private InventoryService inventoryService;

	@GetMapping
	public String home() {
		return "Order Service is working fine...";
	}
	
	@GetMapping("/test")
	public String test() {
		return this.inventoryService.businessLogic("PROD_d3b9b8b8-0b7e-4", 1000);
	}
}
