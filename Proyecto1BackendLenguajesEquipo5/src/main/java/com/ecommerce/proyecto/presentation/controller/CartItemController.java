package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.CartItemService;
import com.ecommerce.proyecto.domain.dto.CartItemDto;
import com.ecommerce.proyecto.domain.request.CartItemRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

	private final CartItemService cartItemService;

	public CartItemController(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<CartItemDto>> findByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(cartItemService.findByUserId(userId));
	}

	@PostMapping("/user/{userId}")
	public ResponseEntity<CartItemDto> addItem(
			@PathVariable Long userId,
			@Valid @RequestBody CartItemRequest request
	) {
		CartItemDto created = cartItemService.addItem(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/user/{userId}/product/{productId}")
	public ResponseEntity<CartItemDto> updateQuantity(
			@PathVariable Long userId,
			@PathVariable Long productId,
			@RequestParam Integer quantity
	) {
		return ResponseEntity.ok(
				cartItemService.updateQuantity(userId, productId, quantity)
		);
	}

	@DeleteMapping("/user/{userId}/product/{productId}")
	public ResponseEntity<Void> removeItem(
			@PathVariable Long userId,
			@PathVariable Long productId
	) {
		boolean removed = cartItemService.removeItem(userId, productId);
		return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
		cartItemService.clearCart(userId);
		return ResponseEntity.noContent().build();
	}
}
