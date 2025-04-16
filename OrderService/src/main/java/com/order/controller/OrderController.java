package com.order.controller;

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
import com.order.entities.Order;
import com.order.in.dto.OrderInDto;
import com.order.out.dto.OrderOutDto;
import com.order.services.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PubSubTemplate pubSubTemplate;
	
	@PostMapping
	public ResponseEntity<OrderOutDto> saveOrder(@RequestBody OrderInDto order) throws Exception{
		log.info("Saving new order for user -> {}", order.getUserId());
		return ResponseEntity.ok(this.orderService.saveOrder(order));
	}
	
	@PutMapping
	public ResponseEntity<OrderOutDto> updateOrder(@RequestParam String orderId, @RequestParam String status){
		log.info("Updating orderId: {} with status: {}", orderId, status);
		return ResponseEntity.ok(this.orderService.updateOrder(orderId, status));
	}
	
	@GetMapping
	public ResponseEntity<List<OrderOutDto>> getAllOrders(){
		log.info("Fetching all orders");
		return ResponseEntity.ok(this.orderService.getAllOrders());
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderOutDto> getOrderByOrderId(@PathVariable("orderId") String orderId){
		log.info("Fetching order by Order ID -> {}", orderId);
		return ResponseEntity.ok(this.orderService.getOrderbyOrderId(orderId));
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderOutDto>> getOrdersByUserId(@PathVariable("userId") String userId){
		log.info("Fetching orders by User ID -> {}", userId);
		return ResponseEntity.ok(this.orderService.getOrderbyUserId(userId));
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable("orderId") String orderId){
		log.info("Deleting order of Order ID -> {}", orderId);
		return ResponseEntity.ok(this.orderService.deleteOrder(orderId));
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<OrderOutDto>> getOrderByProductId(@PathVariable("productId") String productId){
		log.info("Fetching orders by Product ID -> {}", productId);
		List<OrderOutDto> orderoutDtos = this.orderService.getOrderbyProductId(productId);
		return ResponseEntity.ok(orderoutDtos);
		
	}
	
	@PostMapping("/placeorder")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        pubSubTemplate.publish("order-topic", order.toString());
        return ResponseEntity.ok("Order placed!");
    }
}
