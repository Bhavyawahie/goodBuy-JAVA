package com.goodbuy.store.controllers;

import com.goodbuy.store.dto.ErrorResponseDTO;
import com.goodbuy.store.dto.ProductDTO;
import com.goodbuy.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping(value = "/")
	public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) String keyword,
														@RequestParam(required = false) String category,
														@RequestParam(required = false) Integer pageNumber) {
		List<ProductDTO> products;
		if (pageNumber != null) {
			Pageable pageable = PageRequest.of(pageNumber, 5);
			Page<ProductDTO> productPage = productService.getFilteredProducts(keyword, category, pageable);
			products = productPage.getContent();
		} else {
			if (keyword != null && category != null) {
				products = productService.getProductByCategoryAndKeyword(category, keyword);
			} else if (keyword != null) {
				products = productService.getProductByKeyword(keyword);
			} else if (category != null) {
				products = productService.getProductByCategory(category);
			} else {
				products = productService.getAllProducts();
			}
		}
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping(value ="/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable long id) {
		Optional<ProductDTO> product = productService.getProductById(id);
		if (product.isEmpty()) {
			ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder().error(Map.of("message", "product with " + id + " doent'nt exist!")).build();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(product.get(), HttpStatus.OK);
	}
}
