package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.ProductService;
import com.ecommerce.proyecto.domain.dto.ProductDto;
import com.ecommerce.proyecto.domain.request.ProductRequest;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<ProductDto> findAll() {
		return productService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
		return productService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		return productService.update(id, request)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return productService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
