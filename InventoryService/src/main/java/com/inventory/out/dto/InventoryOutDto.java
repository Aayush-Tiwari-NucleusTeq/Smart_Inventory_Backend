package com.inventory.out.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryOutDto {

	private String productId;
	private int stock;
	@Override
	public String toString() {
		return "{" + "\"productId\":\"" + productId + "\"," + "\"stock\":" + stock + "}";
	}
	
	
}
