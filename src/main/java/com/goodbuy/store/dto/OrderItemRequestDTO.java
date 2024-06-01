package com.goodbuy.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDTO {
	private Long product;
	private String name;
	private Integer qty;
	private String image;
	private Double price;
}
