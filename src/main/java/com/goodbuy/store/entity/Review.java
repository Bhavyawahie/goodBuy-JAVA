package com.goodbuy.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private Double rating;
	private String comment;
	@ManyToOne(fetch = FetchType.LAZY) // Many reviews can have one product
	@JoinColumn(name = "productId") // Specify foreign key column
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	@Column(nullable = true)
	private Date createdAt;
}
