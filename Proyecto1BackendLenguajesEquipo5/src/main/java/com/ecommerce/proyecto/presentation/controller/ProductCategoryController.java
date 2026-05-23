package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.ProductCategoryService;
import com.ecommerce.proyecto.domain.dto.ProductCategoryDto;
import com.ecommerce.proyecto.domain.request.ProductCategoryRequest;
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
@RequestMapping("/api/categories")
public class ProductCategoryController {

	private final ProductCategoryService categoryService;

	public ProductCategoryController(ProductCategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public List<ProductCategoryDto> findAll() {
		return categoryService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductCategoryDto> findById(@PathVariable Long id) {
		return categoryService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ProductCategoryDto> create(@Valid @RequestBody ProductCategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductCategoryDto> update(@PathVariable Long id, @Valid @RequestBody ProductCategoryRequest request) {
		return categoryService.update(id, request)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return categoryService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
