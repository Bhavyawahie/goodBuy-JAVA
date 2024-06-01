package com.goodbuy.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private long _id;
	private long user;
	private String name;
	private String brand;
	private String category;
	private String description;
	private Double rating;
	private Long numReviews;
	private String image;
	private Long price;
	private Long countInStock;
	private List<ReviewDTO> reviews;
}
