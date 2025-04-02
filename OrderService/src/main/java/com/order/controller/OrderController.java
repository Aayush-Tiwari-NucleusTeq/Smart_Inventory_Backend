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

import com.order.entities.Order;
import com.order.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<Order> saveOrder(@RequestBody Order order){
		return ResponseEntity.ok(this.orderService.saveOrder(order));
	}
	
	@PutMapping
	public ResponseEntity<Order> updateOrder(@RequestParam String orderId, @RequestParam String status){
		return ResponseEntity.ok(this.orderService.updateOrder(orderId, status));
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderByOrderId(@PathVariable("orderId") String orderId){
		return ResponseEntity.ok(this.orderService.getOrderbyOrderId(orderId));
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("userId") String userId){
		return ResponseEntity.ok(this.orderService.getOrderbyUserId(userId));
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable("orderId") String orderId){
		return ResponseEntity.ok(this.orderService.deleteOrder(orderId));
	}
}
