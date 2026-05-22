package com.ecommerce.Proyecto1BackendLenguajesEquipo5.controller;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CartDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CartRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.service.CartService;
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
@RequestMapping("/api/carts")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping
	public List<CartDto> findAll() {
		return cartService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CartDto> findById(@PathVariable Long id) {
		return cartService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<CartDto> findByUserId(@PathVariable Long userId) {
		return cartService.findByUserId(userId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CartDto> create(@Valid @RequestBody CartRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return cartService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
