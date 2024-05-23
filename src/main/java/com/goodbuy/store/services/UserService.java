package com.goodbuy.store.services;

import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.UserAdminOnlyUpdateDTO;
import com.goodbuy.store.dto.UserDTO;
import com.goodbuy.store.dto.UserUpdateDTO;
import com.goodbuy.store.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;

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

	public UserDTO updateUserById(long id, UserAdminOnlyUpdateDTO userChanges) {
		Optional<User> foundUser = userDAO.findById(id);
		if(foundUser.isPresent()) {
			User user = foundUser.get();
			if(userChanges.getName() != null && !userChanges.getName().isEmpty()) {
				user.setName(userChanges.getName());
			}
			if(userChanges.getEmail() != null && !userChanges.getEmail().isEmpty()) {
				user.setEmail(userChanges.getEmail());
			}
			if(userChanges.getRole() != null && !userChanges.getRole().toString().isEmpty()){
				user.setRole(userChanges.getRole());
			}
			User savedUser = userDAO.save(user);
			return convertToDTO(savedUser);
		}
		return null;
	}

	public UserDTO updateUserProfile(long id, UserUpdateDTO userChanges) {
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
			return convertToDTO(savedUser);
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
