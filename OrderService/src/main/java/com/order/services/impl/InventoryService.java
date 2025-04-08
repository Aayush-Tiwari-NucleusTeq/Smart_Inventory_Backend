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
//		List<Order> orders = this.orderService.getOrdersByProductId(productId);
//		System.out.println(orders);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Business implementation";
	}
}
