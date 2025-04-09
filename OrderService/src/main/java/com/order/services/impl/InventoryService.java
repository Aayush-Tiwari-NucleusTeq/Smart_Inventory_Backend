package com.order.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.Order;
import com.order.services.InventoryClient;
import com.order.services.OrderService;

@Service
public class InventoryService {
	
	@Autowired
	private OrderService orderService;
	
	public String businessLogic(String productId, int stock) {
		System.out.println("Under Inventory service" + productId);
		List<Order> orders = this.orderService.getOrdersByProductId(productId);
		for(Order order : orders) {
			if(stock >= order.getOrderItems().get(0).getQuantity()) {
				System.out.println("The stock left is " + (stock-order.getOrderItems().get(0).getQuantity()));
				String result = this.orderService.updateInventoryAfterAvailability(productId, stock-order.getOrderItems().get(0).getQuantity());
				System.out.println(result);
				order.setStatus("Serviceable");
				this.orderService.updateOrder(order.getOrderId(), "Serviceable");
			}
			
		}
		return "Business implementation";
	}
}
