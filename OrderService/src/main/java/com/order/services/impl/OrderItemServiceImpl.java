package com.order.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.OrderItem;
import com.order.repository.OrderItemRepository;
import com.order.services.OrderItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderItemServiceImpl implements OrderItemService {
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public List<OrderItem> getOrderItemsByProductId(String productId) {
		log.info("Getting the Order Items of the Product ID -> {}", productId);
		return this.orderItemRepository.findByProductId(productId);
	}

}
