package com.goodbuy.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
	private Long userId;
	private List<OrderItemRequestDTO> orderItems;
	private ShippingAddressDTO shippingAddress;
	private String paymentMethod;
	private PaymentResultDTO paymentResult;
	private Double taxPrice;
	private Double shippingPrice;
	private Double totalPrice;
	private Boolean isPaid = false;
	private Date paidAt;
	private Boolean isDelivered = false;
	private Date deliveredAt;
}
