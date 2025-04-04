package com.order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.order.entities.OrderItem;
import com.order.services.OrderItemService;
import com.order.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping
	public ResponseEntity<Order> saveOrder(@RequestBody Order order){
		return ResponseEntity.ok(this.orderService.saveOrder(order));
	}
	
	@PutMapping
	public ResponseEntity<Order> updateOrder(@RequestParam String orderId, @RequestParam String status){
		return ResponseEntity.ok(this.orderService.updateOrder(orderId, status));
	}
	
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders(){
		return ResponseEntity.ok(this.orderService.getAllOrders());
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
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Order>> getOrderByProductId(@PathVariable("productId") String productId){
		List<OrderItem> orderItems = this.orderItemService.getOrderItemsByProductId(productId);
//		orderItems.stream().map(orderId )
		Set<String> orderIds = orderItems.stream()
                .map(orderItem -> orderItem.getOrder().getOrderId()) // Extract orderId
                .collect(Collectors.toSet());
		List<Order> orders = new ArrayList<>();
		System.out.println(orderIds);
		for(String orderId : orderIds) {
			Order order = this.orderService.getOrderbyOrderId(orderId);
			orders.add(order);
		}
		return ResponseEntity.ok(orders);
		
	}
}
