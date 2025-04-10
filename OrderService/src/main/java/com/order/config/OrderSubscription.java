package com.order.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.netflix.discovery.converters.Auto;
import com.order.controller.OrderController;
import com.order.entities.Inventory;
import com.order.entities.Order;
import com.order.services.OrderService;
import com.order.services.impl.InventoryService;

import jakarta.annotation.PostConstruct;

@Component
public class OrderSubscription {

	@Autowired
	private PubSubTemplate pubSubTemplate;
	
	@Autowired
	private InventoryService inventoryService;

	@EventListener(ApplicationReadyEvent.class)
	public void subscribe() {
		Executors.newSingleThreadExecutor().submit(()->{
			try {
				Thread.sleep(5000);
				pubSubTemplate.subscribe("order-subscription", message -> {
			           String data = message.getPubsubMessage().getData().toStringUtf8();
			           String productId = data.substring(14, 34);
			           int stock = Integer.parseInt(data.substring(44, data.length()-1));
			           this.inventoryService.businessLogic(productId, stock);
			           message.ack();
			       });
			}
			catch(Exception ex) {
				System.err.println("Error initializing subscriber : " + ex.getMessage());
			}
		});
	       
	}
}
