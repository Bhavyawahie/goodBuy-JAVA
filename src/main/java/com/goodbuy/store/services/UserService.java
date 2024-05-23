package com.goodbuy.store.services;

import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.UserDTO;
import com.goodbuy.store.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	public Optional<UserDTO> getUserById(long id) {
		return userDAO.findById(id).map(this::convertToDTO);
	}

	public Map<String, String> deleteUserById(long id) {
		userDAO.deleteById(id);
		return Map.of("message", "User deleted successfully");
	}

	public Object updateUserById(long id, UserDTO userDTO) {
		return null;
	}

	private UserDTO convertToDTO(User user) {
		UserDTO userDTO = UserDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.isAdmin(Objects.equals(user.getRole().toString(), "ADMIN"))
				.build();
		return userDTO;
	}
}
