package com.order.config;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.order.entities.Inventory;

import jakarta.annotation.PostConstruct;

@Component
public class OrderSubscription {

	@Autowired
	private PubSubTemplate pubSubTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void subscribe() {
		Executors.newSingleThreadExecutor().submit(()->{
			try {
				Thread.sleep(2000);
				pubSubTemplate.subscribe("order-subscription", message -> {
			           String data = message.getPubsubMessage().getData().toStringUtf8();
			           System.out.println("Inventory updated - " + data);
			           System.out.println(data.substring(14, 34) +" "+ data.substring(44, data.length()-1));
			           message.ack();
			       });
			}
			catch(Exception ex) {
				System.err.println("Error initializing subscriber : " + ex.getMessage());
			}
		});
	       
	}
}
