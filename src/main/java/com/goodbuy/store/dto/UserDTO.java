package com.goodbuy.store.dto;

import com.goodbuy.store.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private Long id;
	private String name;
	private String email;
	private Role role;
}
