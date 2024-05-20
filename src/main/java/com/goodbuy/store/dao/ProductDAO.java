package com.goodbuy.store.dao;

import com.goodbuy.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductsDAO extends JpaRepository<Product, Long> {
	public List<Product> findByKeywordContainingIgnoreCase(String keyword);

	public List<Product> findByCategory(String category);

	public List<Product> findByCategoryAndKeywordContainingIgnoreCase(String category, String keyword);

	public Page<Product> findByKeywordContainingIgnoreCase(String keyword, Pageable pageable);

	public Page<Product> findByCategory(String category, Pageable pageable);

	public Page<Product> findByCategoryAndKeywordContainingIgnoreCase(String category, String keyword, Pageable pageable);
}
