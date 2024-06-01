package com.goodbuy.store.dto;

import com.goodbuy.store.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthResponseDTO {
	private Long id;
	private String name;
	private String email;
	private Boolean isAdmin;
	private String token;
}

