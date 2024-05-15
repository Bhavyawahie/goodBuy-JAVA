package com.goodbuy.store.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController {
	@GetMapping(value = "/")
	public ResponseEntity<List> getProducts() {
		List<String> listOfProducts = List.of("apple", "google", "samsung", "yahoo");
		return new ResponseEntity<>(listOfProducts, HttpStatus.OK);
	}
}
