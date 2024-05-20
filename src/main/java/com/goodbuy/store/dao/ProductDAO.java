package com.goodbuy.store.dao;

import com.goodbuy.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductDAO extends JpaRepository<Product, Long> {
	public List<Product> findByNameContainingIgnoreCase(String name);

	public List<Product> findByCategory(String category);

	@Query("SELECT p FROM Product p WHERE p.category = :category AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
	public List<Product> findByCategoryAndKeywordContainingIgnoreCase(String category, String keyword);

	public Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

	public Page<Product> findByCategory(String category, Pageable pageable);

	public Page<Product> findByCategoryAndNameContainingIgnoreCase(String category, String name, Pageable pageable);
}
