package com.order.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.Order;
import com.order.services.OrderService;

@Service
public class InventoryService {
	
	@Autowired
	private OrderService orderService;
	
	public String businessLogic(String productId, int stock) {
		System.out.println("Under Inventory service" + productId);
		List<Order> orders = this.orderService.getOrdersByProductId(productId);
		for(Order order : orders) {
			System.out.println(order.getOrderId());
			System.out.println(order.getStatus());
			System.out.println(order.getStatus());
			System.out.println(order.getOrderItems().get(0).getQuantity());
			if(stock >= order.getOrderItems().get(0).getQuantity()) {
				order.setStatus("Serviceable");
			}
			this.orderService.updateOrder(order.getOrderId(), "Serviceable");
		}
		return "Business implementation";
	}
}
