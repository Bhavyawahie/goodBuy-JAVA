package com.goodbuy.store.services;

import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.UserAuthResponseDTO;
import com.goodbuy.store.dto.UserLoginDTO;
import com.goodbuy.store.dto.UserRegistrationDTO;
import com.goodbuy.store.entity.Role;
import com.goodbuy.store.entity.User;
import com.goodbuy.store.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserAuthService {

	@Autowired
	private final JwtService jwtService;
	@Autowired
	private final UserDAO userDAO;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final AuthenticationManager authenticationManager;

	public UserAuthResponseDTO registerUser(UserRegistrationDTO userDTO) {
		var user = User.builder().name(userDTO.getName()).email(userDTO.getEmail()).password(passwordEncoder.encode(userDTO.getPassword())).role(userDTO.getRole()).build();
		var savedUser = userDAO.save(user);
		Boolean admin = user.getRole().equals(Role.ADMIN);
		Map<String, Object> claims = new HashMap<>();
		claims.put("isAdmin", admin);
		claims.put("userId", user.getId());
		var jwtToken = jwtService.generateToken(claims, user);
		return UserAuthResponseDTO.builder().id(savedUser.getId()).name(savedUser.getName()).email(savedUser.getEmail()).isAdmin(Objects.equals(user.getRole().toString(), "ADMIN")).token(jwtToken).build();
	}

	public UserAuthResponseDTO loginUser(UserLoginDTO userLoginDTO) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
		var user = userDAO.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User " + userLoginDTO.getEmail() + " not found"));
		Boolean admin = user.getRole().equals(Role.ADMIN);
		Map<String, Object> claims = new HashMap<>();
		claims.put("isAdmin", admin);
		claims.put("userId", user.getId());
		var jwtToken = jwtService.generateToken(claims, user);
		return UserAuthResponseDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).isAdmin(Objects.equals(user.getRole().toString(), "ADMIN")).token(jwtToken).build();

	}

	public Boolean findUserByEmail(String email) {
		return userDAO.findByEmail(email).isPresent();
	}
}

