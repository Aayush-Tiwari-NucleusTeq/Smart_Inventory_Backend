package com.order.services.impl;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.Order;
import com.order.repository.OrderRepository;
import com.order.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order saveOrder(Order order) {
		String orderId = "Order_" + UUID.randomUUID().toString().substring(0, 10);
		order.setOrderId(orderId);
		return this.orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		return this.orderRepository.findAll();
	}

	@Override
	public Order getOrderbyOrderId(String orderId) {
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
