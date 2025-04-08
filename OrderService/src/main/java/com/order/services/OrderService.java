package com.order.services;

import java.util.List;

import com.order.entities.Order;

public interface OrderService {

	Order saveOrder(Order order);
	List<Order> getAllOrders();
	Order getOrderbyOrderId(String orderId);
	List<Order> getOrderbyUserId(String userId);
	Order updateOrder(String orderId, String status);
	String deleteOrder(String orderId);
	List<Order> getOrdersByProductId(String productId);
}
