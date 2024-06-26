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
public class OrderDTO {
	private Long _id;
	private UserDTO user;
	private List<OrderItemResponseDTO> orderItems;
	private ShippingAddressDTO shippingAddress;
	private String paymentMethod;
	private PaymentResultDTO paymentResult;
	private Double taxPrice;
	private Double shippingPrice;
	private Double totalPrice;
	private Boolean isPaid;
	private Date paidAt;
	private Boolean isDelivered;
	private Date deliveredAt;
	private Date createdAt;
}
