package com.order.services;

import java.util.List;

import com.order.entities.Order;
import com.order.in.dto.OrderInDto;
import com.order.out.dto.OrderOutDto;

public interface OrderService {

	OrderOutDto saveOrder(OrderInDto order) throws Exception;
	List<OrderOutDto> getAllOrders();
	OrderOutDto getOrderbyOrderId(String orderId);
	List<OrderOutDto> getOrderbyUserId(String userId);
	List<OrderOutDto> getOrderbyProductId(String productId);
	OrderOutDto updateOrder(String orderId, String status);
	String deleteOrder(String orderId);
	List<OrderOutDto> getOrdersByProductId(String productId);
	String updateInventoryAfterAvailability(String productId, int stock);
	Order orderOutDtoToOrder(OrderOutDto order);
}
