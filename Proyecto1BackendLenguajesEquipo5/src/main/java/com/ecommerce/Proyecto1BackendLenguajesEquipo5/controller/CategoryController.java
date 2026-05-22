package com.ecommerce.Proyecto1BackendLenguajesEquipo5.controller;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CategoryDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CategoryRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.service.CategoryService;
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
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public List<CategoryDto> findAll() {
		return categoryService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
		return categoryService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
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
