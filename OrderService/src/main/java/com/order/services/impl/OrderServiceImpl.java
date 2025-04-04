package com.order.services.impl;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.Inventory;
import com.order.entities.Order;
import com.order.entities.OrderItem;
import com.order.repository.OrderRepository;
import com.order.services.InventoryClient;
import com.order.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private InventoryClient inventoryClient;

	@Override
	public Order saveOrder(Order order) {
		String orderId = "Order_" + UUID.randomUUID().toString().substring(0, 10);
		order.setOrderId(orderId);
		for(OrderItem orderItem: order.getOrderItems()) {
			orderItem.setOrder(order);
		}
		return this.orderRepository.save(order);
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
//		System.out.println(orderItems);
//		List<OrderItem> newOrderItems = orderItems.stream().map(orderItem -> {
//			orderItem.setInventory(inventoryClient.getInventories(orderItem.getProductId()));
//			return orderItem;
//		}).collect(Collectors.toList());
//		System.out.println(newOrderItems);
		return this.orderRepository.findById(orderId).orElseThrow(()-> new ResolutionException("Order with orderID - [" + "] is not  present in the Database!! Please check the id..."));
	}

	@Override
	public List<Order> getOrderbyUserId(String userId) {
		return this.orderRepository.findByUserId(userId);
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

}
