package com.goodbuy.store.services;

import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.UserAuthResponseDTO;
import com.goodbuy.store.dto.UserLoginDTO;
import com.goodbuy.store.dto.UserRegistrationDTO;
import com.goodbuy.store.entity.Role;
import com.goodbuy.store.entity.User;
import com.goodbuy.store.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final JwtService jwtService;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserAuthResponseDTO registerUser(UserRegistrationDTO userDTO) {
        var user = User.builder().name(userDTO.getName()).email(userDTO.getEmail()).password(passwordEncoder.encode(userDTO.getPassword())).role(Role.USER).build();
        var savedUser = userDAO.save(user);
        var jwtToken = jwtService.generateToken(user);
        return UserAuthResponseDTO.builder().id(savedUser.getId()).name(savedUser.getName()).email(savedUser.getEmail()).role(savedUser.getRole()).token(jwtToken).build();
    }

    public UserAuthResponseDTO loginUser(UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
        var user = userDAO.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User " + userLoginDTO.getEmail() + " not found"));
        var jwtToken = jwtService.generateToken(user);
        return UserAuthResponseDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).token(jwtToken).build();

    }

    public Boolean findUserByEmail(String email) {
        return userDAO.findByEmail(email).isPresent();
    }
}

