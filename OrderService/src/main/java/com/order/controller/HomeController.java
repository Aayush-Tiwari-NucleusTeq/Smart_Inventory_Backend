package com.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otest")
public class HomeController {

	@GetMapping
	public String home() {
		return "Order Service is working fine...";
	}
}
