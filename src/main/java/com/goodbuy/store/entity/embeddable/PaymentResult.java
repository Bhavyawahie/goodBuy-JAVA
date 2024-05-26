package com.goodbuy.store.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentResult {
	private Long id;
	private String emailAddress;
	private String status;
	private Date updateTime;
}
