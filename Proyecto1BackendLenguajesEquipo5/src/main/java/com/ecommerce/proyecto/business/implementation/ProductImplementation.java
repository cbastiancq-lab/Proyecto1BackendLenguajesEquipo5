package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.ProductService;
import com.ecommerce.proyecto.domain.model.Product;
import com.ecommerce.proyecto.domain.model.ProductCategory;
import com.ecommerce.proyecto.data.repository.ProductCategoryRepository;
import com.ecommerce.proyecto.data.repository.ProductRepository;
import com.ecommerce.proyecto.domain.dto.ProductDto;
import com.ecommerce.proyecto.domain.request.ProductRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductImplementation implements ProductService {

	private final ProductRepository productRepository;
	private final ProductCategoryRepository categoryRepository;

	public ProductImplementation(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDto> findAll() {
		return productRepository.findAll().stream().map(this::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProductDto> findById(Long id) {
		return productRepository.findById(id).map(this::toDto);
	}

	@Override
	@Transactional
	public ProductDto create(ProductRequest request) {
		Product product = new Product();
		applyRequest(product, request);
		return toDto(productRepository.save(product));
	}

	@Override
	@Transactional
	public Optional<ProductDto> update(Long id, ProductRequest request) {
		return productRepository.findById(id)
				.map(product -> {
					applyRequest(product, request);
					return toDto(productRepository.save(product));
				});
	}

	@Override
	@Transactional
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

	private ProductCategory resolveCategory(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found"));
	}

	private ProductDto toDto(Product product) {
		ProductCategory category = product.getCategory();
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
