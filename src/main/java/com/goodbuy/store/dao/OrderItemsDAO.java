package com.goodbuy.store.dao;

import com.goodbuy.store.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsDAO extends JpaRepository<OrderItem, Long> {
}
