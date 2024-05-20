package com.goodbuy.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private long id;
	private UserDTO user;
	private String name;
	private String brand;
	private String category;
	private String description;
	private Double rating;
	private Long numReviews;
	private String image;
	private Long price;
	private Long countInStock;
}
