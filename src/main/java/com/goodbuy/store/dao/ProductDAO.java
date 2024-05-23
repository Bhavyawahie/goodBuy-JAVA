package com.goodbuy.store.dao;

import com.goodbuy.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ProductDAO extends JpaRepository<Product, Long> {
	List<Product> findByNameContainingIgnoreCase(String name);

	List<Product> findByCategory(String category);

	@Query("SELECT p FROM Product p WHERE p.category = :category AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
	List<Product> findByCategoryAndKeywordContainingIgnoreCase(String category, String keyword);

	Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

	Page<Product> findByCategory(String category, Pageable pageable);

	Page<Product> findByCategoryAndNameContainingIgnoreCase(String category, String name, Pageable pageable);
}
