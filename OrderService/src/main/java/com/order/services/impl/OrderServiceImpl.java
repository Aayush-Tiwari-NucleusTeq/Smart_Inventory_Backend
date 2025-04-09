package com.order.services.impl;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.order.entities.Inventory;
import com.order.entities.Order;
import com.order.entities.OrderItem;
import com.order.repository.OrderRepository;
import com.order.services.InventoryClient;
import com.order.services.OrderItemService;
import com.order.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private InventoryClient inventoryClient;

	@Override
	public Order saveOrder(Order order) {
		String orderId = "Order_" + UUID.randomUUID().toString().substring(0, 10);
		order.setOrderId(orderId);
		List<OrderItem> orderItems = order.getOrderItems();
		List<String> productIds = new ArrayList<>();
		List<String> status = new ArrayList<>();
		for(OrderItem orderit: orderItems) {
			productIds.add(orderit.getProductId());
			Inventory inventory = this.inventoryClient.getInventories(orderit.getProductId());
			if(orderit.getQuantity() <= inventory.getStock()) {
				this.inventoryClient.updateInventory(inventory.getProductId(), inventory.getStock()-orderit.getQuantity());
				status.add("Serviceable");
			} else {
				status.add("Non-serviceable");
			}
		}
		System.out.println(status);
		
		for(OrderItem orderItem: order.getOrderItems()) {
			orderItem.setOrder(order);
		}
		if(status.contains("Non-serviceable")) {
			order.setStatus("Non-serviceable");
		} else {
			order.setStatus("Serviceable");
		}
//		System.out.println(order);
		return this.orderRepository.save(order);
//		return null;
	}

	@Override
	public List<Order> getAllOrders() {
		return this.orderRepository.findAll();
	}

	@Override
	public Order getOrderbyOrderId(String orderId) {
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ResolutionException("Order with orderID - [" + "] is not  present in the Database!! Please check the id..."));
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem orderitem : orderItems) {
			Inventory inventory = this.inventoryClient.getInventories(orderitem.getProductId());
			orderitem.setInventory(inventory);
		}
//		System.out.println(orderItems);
		for(OrderItem orderitem : orderItems) {
			System.out.println(orderitem.getInventory().getStock() +" "+ orderitem.getInventory().getProductId());
		}
		return this.orderRepository.findById(orderId).orElseThrow(()-> new ResolutionException("Order with orderID - [" + "] is not  present in the Database!! Please check the id..."));
	}

	@Override
	public List<Order> getOrderbyUserId(String userId) {
		return this.orderRepository.findByUserId(userId);
	}
	
	public List<Order> getOrdersByProductId(String productId){
		List<OrderItem> orderItems = this.orderItemService.getOrderItemsByProductId(productId);
//		orderItems.stream().map(orderId)
		Set<String> orderIds = orderItems.stream()
                .map(orderItem -> orderItem.getOrder().getOrderId()) 
                .collect(Collectors.toSet());
		List<Order> orders = new ArrayList<>();
		for(String orderId : orderIds) {
			Order order = this.getOrderbyOrderId(orderId);
			orders.add(order);
		}
		return orders;
	}

	@Override
	public Order updateOrder(String orderId, String status) {
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ResolutionException("Order with orderID - [" + "] is not  present in the Database!! Please check the id..."));
		order.setStatus(status);
		this.orderRepository.save(order);
		return order;
	}

	@Override
	public String deleteOrder(String orderId) {
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ResolutionException("Order with orderID - [" + "] is not  present in the Database!! Please check the id..."));
		if(order != null) {
			this.orderRepository.deleteById(orderId);
			return "Order is deleted successfully";
		} else {
			return "Something went wrong";
		}
	}

	@Override
	public String updateInventoryAfterAvailability(String productId, int stock) {
		this.inventoryClient.updateInventory(productId, stock);
		System.out.println("===============================================================" + productId +" "+ stock);
		return "Updated inventory";
	}

}
