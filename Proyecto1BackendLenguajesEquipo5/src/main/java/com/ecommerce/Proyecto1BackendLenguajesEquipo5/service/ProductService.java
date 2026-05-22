package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.ProductDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.ProductRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.Category;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.Product;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository.CategoryRepository;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	public List<ProductDto> findAll() {
		return productRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	public Optional<ProductDto> findById(Long id) {
		return productRepository.findById(id).map(this::toDto);
	}

	public ProductDto create(ProductRequest request) {
		Product product = new Product();
		applyRequest(product, request);
		return toDto(productRepository.save(product));
	}

	public Optional<ProductDto> update(Long id, ProductRequest request) {
		return productRepository.findById(id)
				.map(product -> {
					applyRequest(product, request);
					return toDto(productRepository.save(product));
				});
	}

	public boolean delete(Long id) {
		if (!productRepository.existsById(id)) {
			return false;
		}
		productRepository.deleteById(id);
		return true;
	}

	private void applyRequest(Product product, ProductRequest request) {
		product.setName(request.name());
		product.setDescription(request.description());
		product.setPrice(request.price());
		product.setStock(request.stock());
		product.setActive(request.active() == null || request.active());
		product.setCategory(resolveCategory(request.categoryId()));
	}

	private Category resolveCategory(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found"));
	}

	private ProductDto toDto(Product product) {
		Category category = product.getCategory();
		return new ProductDto(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getStock(),
				product.isActive(),
				category == null ? null : category.getId(),
				category == null ? null : category.getName()
		);
	}
}
