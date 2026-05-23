package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.OrderService;
import com.ecommerce.proyecto.domain.request.OrderRequest;
import com.ecommerce.proyecto.domain.response.OrderResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public List<OrderResponse> findAll() {
		return orderService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
		return orderService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/user/{userId}")
	public List<OrderResponse> findByUserId(@PathVariable Long userId) {
		return orderService.findByUserId(userId);
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return orderService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
