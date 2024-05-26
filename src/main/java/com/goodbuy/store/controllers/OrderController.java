package com.goodbuy.store.controllers;

import com.goodbuy.store.dto.OrderRequestDTO;
import com.goodbuy.store.dto.PaymentResultDTO;
import com.goodbuy.store.entity.Order;
import com.goodbuy.store.services.OrderService;
import com.goodbuy.store.dto.OrderDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private HttpServletRequest context;

	@PostMapping
	public ResponseEntity<OrderDTO> addOrderItems(@RequestBody OrderRequestDTO orderDTO) {
		OrderDTO createdOrder = orderService.addOrderItems(orderDTO,(Integer) context.getAttribute("userId"));
		return ResponseEntity.status(201).body(createdOrder);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
		OrderDTO order = orderService.getOrderById(id);
		return ResponseEntity.ok(order);
	}

	@PutMapping("/{id}/pay")
	public ResponseEntity<OrderDTO> updateOrderToPaid(@PathVariable Long id, @RequestBody PaymentResultDTO paymentResultDTO) {
		OrderDTO updatedOrder = orderService.updateOrderToPaid(id, paymentResultDTO);
		return ResponseEntity.ok(updatedOrder);
	}

	@PutMapping("/{id}/deliver")
	public ResponseEntity<OrderDTO> updateOrderToDelivered(@PathVariable Long id) {
		OrderDTO updatedOrder = orderService.updateOrderToDelivered(id);
		return ResponseEntity.ok(updatedOrder);
	}

	@GetMapping("/myorders")
	public ResponseEntity<List<OrderDTO>> getMyOrders() {
		List<OrderDTO> orders = orderService.getMyOrders((Integer) context.getAttribute("userId"));
		return ResponseEntity.ok(orders);
	}

	@GetMapping
	public ResponseEntity<List<OrderDTO>> getOrders() {
		List<OrderDTO> orders = orderService.getOrders();
		return ResponseEntity.ok(orders);
	}
}

