package com.goodbuy.store.services;

import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.UserAuthResponseDTO;
import com.goodbuy.store.dto.UserDTO;
import com.goodbuy.store.dto.UserUpdateDTO;
import com.goodbuy.store.entity.Role;
import com.goodbuy.store.entity.User;
import com.goodbuy.store.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private JwtService jwtService;

	public List<UserDTO> getAllUsers() {
		return userDAO.findAll().stream().map(this::convertToDTO).toList();
	}

	public Optional<UserDTO> getUserById(long id) {
		return userDAO.findById(id).map(this::convertToDTO);
	}

	public Map<String, String> deleteUserById(long id) {
		userDAO.deleteById(id);
		return Map.of("message", "User deleted successfully");
	}

	public UserDTO updateUserById(long id, UserUpdateDTO userChanges) {
		Optional<User> foundUser = userDAO.findById(id);
		if(foundUser.isPresent()) {
			User user = foundUser.get();
			if(userChanges.getName() != null && !userChanges.getName().isEmpty()) {
				user.setName(userChanges.getName());
			}
			if(userChanges.getEmail() != null && !userChanges.getEmail().isEmpty()) {
				user.setEmail(userChanges.getEmail());
			}
			if(userChanges.getIsAdmin() != null) {
				Role role = userChanges.getIsAdmin() ? Role.ADMIN : Role.USER;
				user.setRole(role);
			}
			User savedUser = userDAO.save(user);
			return convertToDTO(savedUser);
		}
		return null;
	}

	public UserAuthResponseDTO updateUserProfile(long id, UserUpdateDTO userChanges) {
		Optional<User> foundUser = userDAO.findById(id);
		if(foundUser.isPresent()) {
			User user = foundUser.get();
			if(userChanges.getName() != null && !userChanges.getName().isEmpty()) {
				user.setName(userChanges.getName());
			}
			if(userChanges.getEmail() != null && !userChanges.getEmail().isEmpty()) {
				user.setEmail(userChanges.getEmail());
			}
			if(userChanges.getPassword() != null && !userChanges.getPassword().isEmpty()) {
				user.setPassword(userChanges.getPassword());
			}
			User savedUser = userDAO.save(user);
			Boolean admin = savedUser.getRole().equals(Role.ADMIN);
			Map<String, Object> claims = new HashMap<>();
			claims.put("isAdmin", admin);
			claims.put("userId", user.getId());
			var jwtToken = jwtService.generateToken(claims, user);
			UserAuthResponseDTO userDTO = UserAuthResponseDTO.builder()
					.id(savedUser.getId())
					.name(savedUser.getName())
					.email(savedUser.getEmail())
					.isAdmin(Objects.equals(savedUser.getRole().toString(), "ADMIN"))
					.token(jwtToken)
					.build();
			return userDTO;
		}
		return null;
	}

	private UserDTO convertToDTO(User user) {
		UserDTO userDTO = UserDTO.builder()
				._id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.isAdmin(Objects.equals(user.getRole().toString(), "ADMIN"))
				.build();
		return userDTO;
	}

}
