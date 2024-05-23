package com.goodbuy.store.dao;

import com.goodbuy.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
