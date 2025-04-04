package com.order.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.OrderItem;
import com.order.repository.OrderItemRepository;
import com.order.services.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public List<OrderItem> getOrderItemsByProductId(String productId) {
		return this.orderItemRepository.findByProductId(productId);
	}

}
