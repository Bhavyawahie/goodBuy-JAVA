package com.goodbuy.store.controllers;

import com.goodbuy.store.dto.*;
import com.goodbuy.store.services.ProductService;
import com.goodbuy.store.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController {
	@Autowired
	private HttpServletRequest context;
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable long id) {
		Optional<UserDTO> user = userService.getUserById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user.get(), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userDTO) {
		Optional<UserDTO> user = userService.getUserById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userService.updateUserById(id, userDTO), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable long id) {
		Optional<UserDTO> user = userService.getUserById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<?> getUserProfile() {
		return new ResponseEntity<>(userService.getUserById((int)context.getAttribute("userId")).get(), HttpStatus.OK);
	}

	@PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserProfile(@RequestBody UserUpdateDTO userDTO) {
		return new ResponseEntity<>(userService.updateUserProfile((int)context.getAttribute("userId"), userDTO), HttpStatus.OK);
	}

}
