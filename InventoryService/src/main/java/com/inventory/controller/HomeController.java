package com.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test")
public class HomeController {

//	@Autowired
//	private PubSubTemplate pubSubTemplate;
	
	@GetMapping
	public String home() {
		log.info("The inventory service is working fine");
		return "The Inventory Service is working fine";
	}
	
//	@GetMapping("/publish")
//	public void test() {
//		this.pubSubTemplate.publish("order-topic", "Random message from Aayush");
//		System.out.println("Publishing message to GCP Console Pub/Sub");
//	}
}
