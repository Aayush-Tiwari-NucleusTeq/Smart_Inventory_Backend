package com.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entities.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

	List<Order> findByUserId(String userId);
}
