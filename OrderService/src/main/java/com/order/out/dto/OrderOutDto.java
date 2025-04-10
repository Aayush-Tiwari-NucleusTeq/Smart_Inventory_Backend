package com.order.out.dto;

import java.util.List;

import com.google.auto.value.AutoValue.Builder;
import com.order.entities.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderOutDto {
	private String orderId;
	private String userId;
	private String status;
	private List<OrderItemOutDto> orderItems;
}
