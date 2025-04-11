package com.order.services.impl;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entities.Inventory;
import com.order.entities.Order;
import com.order.entities.OrderItem;
import com.order.exception.ResourceNotFoundException;
import com.order.in.dto.OrderInDto;
import com.order.in.dto.OrderItemInDto;
import com.order.out.dto.OrderItemOutDto;
import com.order.out.dto.OrderOutDto;
import com.order.repository.OrderRepository;
import com.order.services.InventoryClient;
import com.order.services.OrderItemService;
import com.order.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private InventoryClient inventoryClient;

	@Override
	public OrderOutDto saveOrder(OrderInDto orderInDto) {
		Order order = this.orderInDtoToOrder(orderInDto);
		String orderId = "Order_" + UUID.randomUUID().toString().substring(0, 10);
		order.setOrderId(orderId);
		List<OrderItem> orderItems = order.getOrderItems();
		List<String> productIds = new ArrayList<>();
		List<String> status = new ArrayList<>();
		for(OrderItem orderit: orderItems) {
			productIds.add(orderit.getProductId());
			Inventory inventory = this.inventoryClient.getInventories(orderit.getProductId());
			if(orderit.getQuantity() <= inventory.getStock()) {
				this.inventoryClient.updateInventory(inventory.getProductId(), inventory.getStock()-orderit.getQuantity());
				status.add("Serviceable");
			} else {
				status.add("Non-serviceable");
			}
		}
		System.out.println(status);
		
		for(OrderItem orderItem: order.getOrderItems()) {
			orderItem.setOrder(order);
		}
		if(status.contains("Non-serviceable")) {
			order.setStatus("Non-serviceable");
		} else {
			order.setStatus("Serviceable");
		}
		Order savedOrder = this.orderRepository.save(order);
		OrderOutDto orderOutDto = this.orderToOrderOutDto(savedOrder);
		return orderOutDto;
	}

	@Override
	public List<OrderOutDto> getAllOrders() {
		List<Order> orders = this.orderRepository.findAll();
		List<OrderOutDto> orderOutDtos = orders.stream().map(order -> this.orderToOrderOutDto(order)).collect(Collectors.toList());
		return orderOutDtos;
	}

	@Override
	public OrderOutDto getOrderbyOrderId(String orderId) {
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with orderID - [" + orderId + "] is not  present in the Database!! Please check the id..."));
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem orderitem : orderItems) {
			Inventory inventory = this.inventoryClient.getInventories(orderitem.getProductId());
			orderitem.setInventory(inventory);
		}
		OrderOutDto orderOutDto = this.orderToOrderOutDto(order);
		return orderOutDto;
	}

	@Override
	public List<OrderOutDto> getOrderbyUserId(String userId) {
		List<Order> orders = this.orderRepository.findByUserId(userId);
		List<OrderOutDto> orderOutDtos = orders.stream().map(order -> this.orderToOrderOutDto(order)).collect(Collectors.toList());
		return orderOutDtos;
	}
	
	public List<OrderOutDto> getOrdersByProductId(String productId){
		List<OrderItem> orderItems = this.orderItemService.getOrderItemsByProductId(productId);
//		orderItems.stream().map(orderId)
		Set<String> orderIds = orderItems.stream()
                .map(orderItem -> orderItem.getOrder().getOrderId()) 
                .collect(Collectors.toSet());
		List<Order> orders = new ArrayList<>();
		for(String orderId : orderIds) {
			OrderOutDto orderOutDto = this.getOrderbyOrderId(orderId);
			Order order = this.orderOutDtoToOrder(orderOutDto);
			orders.add(order);
		}
		
		List<OrderOutDto> orderOutDtos = orders.stream().map(order -> this.orderToOrderOutDto(order)).collect(Collectors.toList());
		return orderOutDtos;
	}
	
	@Override
	public List<OrderOutDto> getOrderbyProductId(String productId) {
		List<OrderItem> orderItems = this.orderItemService.getOrderItemsByProductId(productId);
		Set<String> orderIds = orderItems.stream()
                .map(orderItem -> orderItem.getOrder().getOrderId())
                .collect(Collectors.toSet());
		List<Order> orders = new ArrayList<>();
		System.out.println(orderIds);
		for(String orderId : orderIds) {
			OrderOutDto orderOutDto = this.getOrderbyOrderId(orderId);
			Order order = this.orderOutDtoToOrder(orderOutDto);
			orders.add(order);
		}
		List<OrderOutDto> orderOutDtos = orders.stream().map(order -> this.orderToOrderOutDto(order)).collect(Collectors.toList());
		return orderOutDtos;
	}


	@Override
	public OrderOutDto updateOrder(String orderId, String status) {
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ResolutionException("Order with orderID - [" + "] is not  present in the Database!! Please check the id..."));
		order.setStatus(status);
		this.orderRepository.save(order);
		OrderOutDto orderOutDto = this.orderToOrderOutDto(order);
		return orderOutDto;
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

	@Override
	public String updateInventoryAfterAvailability(String productId, int stock) {
		this.inventoryClient.updateInventory(productId, stock);
		return "Updated inventory";
	}
	
	public Order orderInDtoToOrder(OrderInDto orderInDto) {
	    Order order = new Order();
	    order.setUserId(orderInDto.getUserId());
	    List<OrderItem> orderItemList = new ArrayList<>();
	    for (OrderItemInDto itemDto : orderInDto.getOrderItems()) {
	        OrderItem item = new OrderItem();
	        item.setProductId(itemDto.getProductId());
	        item.setQuantity(itemDto.getQuantity());
	        item.setOrder(order);
	        orderItemList.add(item);
	    }

	    order.setOrderItems(orderItemList);
	    return order;
	}
	
	
	public OrderOutDto orderToOrderOutDto(Order order) {
	    OrderOutDto outDto = new OrderOutDto();
	    outDto.setOrderId(order.getOrderId());
	    outDto.setUserId(order.getUserId());
	    outDto.setStatus(order.getStatus());

	    List<OrderItemOutDto> itemOutDtos = new ArrayList<>();
	    for (OrderItem item : order.getOrderItems()) {
	        OrderItemOutDto itemDto = new OrderItemOutDto();
	        itemDto.setOrderItemId(item.getOrderItemId());
	        itemDto.setProductId(item.getProductId());
	        itemDto.setQuantity(item.getQuantity());
	        itemDto.setInventory(item.getInventory()); 
	        itemOutDtos.add(itemDto);
	    }

	    outDto.setOrderItems(itemOutDtos);
	    return outDto;
	}


	@Override
	public Order orderOutDtoToOrder(OrderOutDto outDto) {
	    Order order = new Order();
	    order.setOrderId(outDto.getOrderId());
	    order.setUserId(outDto.getUserId());
	    order.setStatus(outDto.getStatus());

	    List<OrderItem> orderItems = new ArrayList<>();
	    for (OrderItemOutDto itemDto : outDto.getOrderItems()) {
	        OrderItem item = new OrderItem();
	        item.setOrderItemId(itemDto.getOrderItemId());
	        item.setProductId(itemDto.getProductId());
	        item.setQuantity(itemDto.getQuantity());
	        item.setInventory(itemDto.getInventory());
	        item.setOrder(order);
	        orderItems.add(item);
	    }

	    order.setOrderItems(orderItems);
	    return order;
	}
}
