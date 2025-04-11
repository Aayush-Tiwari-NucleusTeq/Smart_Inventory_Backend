package com.order.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.Order;
import com.order.out.dto.OrderOutDto;
import com.order.services.OrderService;

@Service
public class InventoryService {
	
	@Autowired
	private OrderService orderService;
	
	public String businessLogic(String productId, int stock) {
		List<OrderOutDto> orderOutDtos = this.orderService.getOrdersByProductId(productId);
		List<Order> orders = orderOutDtos.stream().map(orderOutDto -> this.orderService.orderOutDtoToOrder(orderOutDto)).collect(Collectors.toList());
		for(Order order : orders) {
			if(stock >= order.getOrderItems().get(0).getQuantity() && order.getStatus()!="Serviceable") {
				System.out.println("Under the for loop");
				String result = this.orderService.updateInventoryAfterAvailability(productId, stock-order.getOrderItems().get(0).getQuantity());
				order.setStatus("Serviceable");
				this.orderService.updateOrder(order.getOrderId(), "Serviceable");
			}
			
		}
		return "Business implementation";
	}
}
