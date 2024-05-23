package com.goodbuy.store.controllers;

import com.goodbuy.store.dto.UserLoginDTO;
import com.goodbuy.store.dto.UserRegistrationDTO;
import com.goodbuy.store.entity.Role;
import com.goodbuy.store.services.UserAuthService;
import com.goodbuy.store.dto.UserAuthResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {
	@Autowired
	private UserAuthService userAuthService;

	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
		if (userAuthService.findUserByEmail(userRegistrationDTO.getEmail())) {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("error", "User already exists");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
		}
		return ResponseEntity.ok(userAuthService.registerUser(userRegistrationDTO));
	}

	@PostMapping("/login")
	public ResponseEntity<UserAuthResponseDTO> authenticateUser(@RequestBody UserLoginDTO userLoginDTO) {
		return ResponseEntity.ok(userAuthService.loginUser(userLoginDTO));
	}
}
