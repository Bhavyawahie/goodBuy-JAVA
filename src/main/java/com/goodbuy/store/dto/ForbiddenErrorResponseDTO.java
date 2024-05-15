package com.goodbuy.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForbiddenErrorResponseDTO {
	private int status;
	private String error;
	private String message;
}