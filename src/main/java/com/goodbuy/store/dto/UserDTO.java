package com.goodbuy.store.dto;

import com.goodbuy.store.entity.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private Long id;
	private String name;
	private String email;
	private Boolean isAdmin;
}
