package com.goodbuy.store.dao;

import com.goodbuy.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);
}
