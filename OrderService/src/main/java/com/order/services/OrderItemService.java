package com.order.services;

import java.util.List;

import com.order.entities.OrderItem;

public interface OrderItemService {

	List<OrderItem> getOrderItemsByProductId(String productId);
}
