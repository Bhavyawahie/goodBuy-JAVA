package com.goodbuy.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
	private String title;
	private Double rating;
	private String comment;
	private Date createdAt;
}
