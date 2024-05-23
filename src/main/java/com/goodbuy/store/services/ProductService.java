package com.goodbuy.store.services;

import com.goodbuy.store.dao.ProductDAO;
import com.goodbuy.store.dto.ProductDTO;
import com.goodbuy.store.dto.UserDTO;
import com.goodbuy.store.entity.Product;
import com.goodbuy.store.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
	@Autowired
	private ProductDAO productDAO;
	public List<ProductDTO> getAllProducts() {
		return productDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProductDTO> getProductByKeyword(String keyword) {
		return productDAO.findByNameContainingIgnoreCase(keyword).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProductDTO> getProductByCategory(String category) {
		return productDAO.findByCategory(category).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProductDTO> getProductByCategoryAndKeyword(String category, String keyword) {
		return productDAO.findByCategoryAndKeywordContainingIgnoreCase(category,keyword).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Page<ProductDTO> getProductByPage(Pageable pageable) {
		return productDAO.findAll(pageable).map(this::convertToDTO);
	}

	public Page<ProductDTO> getFilteredProducts(String keyword, String category, Pageable pageable) {
		if (keyword != null && category != null) {
			return productDAO.findByCategoryAndNameContainingIgnoreCase(category, keyword, pageable).map(this::convertToDTO);
		} else if (keyword != null) {
			return productDAO.findByNameContainingIgnoreCase(keyword, pageable).map(this::convertToDTO);
		} else if (category != null) {
			return productDAO.findByCategory(category, pageable).map(this::convertToDTO);
		} else {
			return productDAO.findAll(pageable).map(this::convertToDTO);
		}
	}

	private ProductDTO convertToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		User productUser = product.getUser();
		UserDTO userDTO = UserDTO.builder()
				.id(productUser.getId())
				.name(productUser.getName())
				.email(productUser.getEmail())
				.isAdmin(Objects.equals(productUser.getRole().toString(), "ADMIN"))
				.build();
		BeanUtils.copyProperties(product, productDTO);
		productDTO.setUser(userDTO);
		return productDTO;
	}

	public Optional<ProductDTO> getProductById(long id) {
		return productDAO.findById(id).map(this::convertToDTO);
	}

	public Map<String, String> deleteProduct(long id) {
		productDAO.deleteById(id);
		return Map.of("message", "Product Deleted");
	}
}
