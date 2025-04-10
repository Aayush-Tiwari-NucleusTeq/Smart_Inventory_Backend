package com.order.out.dto;

import com.google.auto.value.AutoValue.Builder;
import com.order.entities.Inventory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderItemOutDto {
	private int orderItemId;
	private String productId;
	private int quantity;
	private Inventory inventory;
}
