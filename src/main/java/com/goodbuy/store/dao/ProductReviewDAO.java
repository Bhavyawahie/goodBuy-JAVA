package com.goodbuy.store.dao;

import com.goodbuy.store.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewDAO extends JpaRepository<Review, Long> {
	List<Review> findByProductIdAndUserId(long productId, long userId);
}
