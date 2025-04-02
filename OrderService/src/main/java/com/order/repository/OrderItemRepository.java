package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
