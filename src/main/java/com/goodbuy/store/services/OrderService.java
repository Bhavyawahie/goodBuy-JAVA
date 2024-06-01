package com.goodbuy.store.services;

import com.goodbuy.store.dao.OrderDAO;
import com.goodbuy.store.dao.OrderItemsDAO;
import com.goodbuy.store.dao.ProductDAO;
import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.*;
import com.goodbuy.store.entity.Order;
import com.goodbuy.store.entity.OrderItem;
import com.goodbuy.store.entity.embeddable.PaymentResult;
import com.goodbuy.store.entity.embeddable.ShippingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private OrderItemsDAO orderItemsDAO;

	public OrderDTO addOrderItems(OrderRequestDTO orderDTO, long userId) {
		List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(i -> OrderItem.builder().name(i.getName()).productId(i.getProduct()).qty(i.getQty()).image(productDAO.findById(i.getProduct()).get().getImage()).price(Double.valueOf(productDAO.findById(i.getProduct()).get().getPrice())).build()).toList();
		PaymentResult paymentResult = null;
		if (orderDTO.getPaymentResult() != null) {
			PaymentResult.builder().id(orderDTO.getPaymentResult().getId()).emailAddress(orderDTO.getPaymentResult().getEmailAddress()).status(orderDTO.getPaymentResult().getStatus()).updateTime(orderDTO.getPaymentResult().getUpdateTime()).build();
		}
		Order order = Order.builder()
				.user(userDAO.findById(userId).get())
				.shippingAddress(ShippingAddress.builder().address(orderDTO.getShippingAddress().getAddress()).city(orderDTO.getShippingAddress().getCity()).state(orderDTO.getShippingAddress().getState()).pincode(orderDTO.getShippingAddress().getPincode()).build())
				.paymentMethod(orderDTO.getPaymentMethod())
				.paymentResult(paymentResult)
				.taxPrice(orderDTO.getTaxPrice())
				.shippingPrice(orderDTO.getShippingPrice())
				.totalPrice(orderDTO.getTotalPrice())
				.isPaid(orderDTO.getIsPaid())
				.paidAt(Date.from(Instant.now()))
				.isDelivered(orderDTO.getIsDelivered())
				.deliveredAt(null)
				.build();
		Order createdOrder = orderDAO.save(order);
		List<OrderItem> createdOrderItems = orderItems.stream().map(orderItem -> {
			orderItem.setOrderId(createdOrder.getId());
			return orderItem;
		}).toList();
		createdOrder.setOrderItems(orderItemsDAO.saveAll(createdOrderItems));
		return mapToOrderDTO(orderDAO.save(createdOrder));
	}

	public OrderDTO getOrderById(long id) {
		return orderDAO.findById(id).map(this::mapToOrderDTO).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	public OrderDTO updateOrderToPaid(long id, PaymentResultDTO paymentResultDTO) {
		Order order = orderDAO.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		PaymentResult paymentResult = PaymentResult.builder()
				.id(paymentResultDTO.getId())
				.emailAddress(paymentResultDTO.getEmailAddress())
				.status(paymentResultDTO.getStatus())
				.updateTime(paymentResultDTO.getUpdateTime())
				.build();
		order.setPaymentResult(paymentResult);
		order.setIsPaid(paymentResultDTO.getStatus().equals("COMPLETED") ? true : false);
		order.setPaidAt(paymentResultDTO.getUpdateTime());
		return mapToOrderDTO(orderDAO.save(order));
	}

	public OrderDTO updateOrderToDelivered(long id) {
		Order order = orderDAO.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setIsDelivered(true);
		order.setDeliveredAt(new Date());
		return mapToOrderDTO(orderDAO.save(order));
	}

	public List<OrderDTO> getMyOrders(long userId) {
		return orderDAO.findByUserId(userId).stream().map(this::mapToOrderDTO).toList();
	}

	public List<OrderDTO> getOrders() {
		return orderDAO.findAll().stream().map(this::mapToOrderDTO).toList();
	}

	private OrderDTO mapToOrderDTO(Order o) {
		ShippingAddressDTO shippingAddressDTO = ShippingAddressDTO
				.builder()
				.address(o.getShippingAddress().getAddress())
				.city(o.getShippingAddress().getCity())
				.state(o.getShippingAddress().getState())
				.pincode(o.getShippingAddress().getPincode())
				.build();
		PaymentResultDTO paymentResultDTO = null;
		if (o.getPaymentResult() != null) {
			paymentResultDTO = PaymentResultDTO
					.builder()
					.id(o.getPaymentResult().getId())
					.emailAddress(o.getPaymentResult().getEmailAddress())
					.status(o.getPaymentResult().getStatus())
					.updateTime(o.getPaymentResult().getUpdateTime())
					.build();
		}

		OrderDTO orderDTO = OrderDTO.builder()
				._id(o.getId())
				.orderItems(o.getOrderItems().stream().map(i -> {
					OrderItemResponseDTO orderItemDTO = OrderItemResponseDTO.builder()
							._id(i.getId())
							.name(i.getName())
							.qty(i.getQty())
							.image(i.getImage())
							.price(i.getPrice())
							.productId(i.getProductId())
							.build();
					return orderItemDTO;
				}).toList())
				.user(userDAO.findById(o.getUser().getId()).map(user -> UserDTO.builder()._id(o.getUser().getId()).email(o.getUser().getEmail()).name(o.getUser().getName()).isAdmin(Objects.equals(o.getUser().getRole().toString(), "ADMIN")).build()).get())
				.isDelivered(o.getIsDelivered())
				.isPaid(o.getIsPaid())
				.paidAt(o.getPaidAt())
				.deliveredAt(o.getDeliveredAt())
				.totalPrice(o.getTotalPrice())
				.shippingPrice(o.getShippingPrice())
				.taxPrice(o.getTaxPrice())
				.shippingAddress(shippingAddressDTO)
				.paymentResult(paymentResultDTO)
				.paymentMethod(o.getPaymentMethod())
				.createdAt(o.getCreatedAt())
				.build();
		return orderDTO;
	}
}

