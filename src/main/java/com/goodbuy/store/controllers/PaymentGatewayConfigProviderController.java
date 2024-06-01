package com.goodbuy.store.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class PaymentGatewayConfigProviderController {
	@Value("${razorpay_api_key}")
	private String razorpay_key;

	@GetMapping("/api/v1/config/razorpay")
	public ResponseEntity<Map<String, String>> getRazorpayConfig() {
		return new ResponseEntity<>(Map.of("key", razorpay_key), HttpStatus.OK);
	}
}
