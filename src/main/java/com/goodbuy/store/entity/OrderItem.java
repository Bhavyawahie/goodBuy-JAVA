package com.goodbuy.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderItems")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private Long orderId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer qty;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private Double price;

	private Long productId;
}
