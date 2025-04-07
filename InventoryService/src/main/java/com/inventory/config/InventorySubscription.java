package com.inventory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import jakarta.annotation.PostConstruct;

@Component
public class InventorySubscription {

	 @Autowired
	 private PubSubTemplate pubSubTemplate;

	 @PostConstruct
	 public void subscribe() {
	        pubSubTemplate.subscribe("order-subscription", message -> {
	            String data = message.getPubsubMessage().getData().toStringUtf8();
	            System.out.println("Received order: " + data);
	            message.ack();
	        });
	 }
}