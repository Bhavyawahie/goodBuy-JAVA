package com.goodbuy.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="products")
public class Product {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private User user;
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
