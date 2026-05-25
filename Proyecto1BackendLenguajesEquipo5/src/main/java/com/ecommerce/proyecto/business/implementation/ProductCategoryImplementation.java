package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.ProductCategoryService;
import com.ecommerce.proyecto.domain.model.ProductCategory;
import com.ecommerce.proyecto.data.repository.ProductCategoryRepository;
import com.ecommerce.proyecto.domain.dto.ProductCategoryDto;
import com.ecommerce.proyecto.domain.request.ProductCategoryRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCategoryImplementation implements ProductCategoryService {

	private final ProductCategoryRepository categoryRepository;

	public ProductCategoryImplementation(ProductCategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategoryDto> findAll() {
		return categoryRepository.findAll().stream().map(this::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProductCategoryDto> findById(Long id) {
		return categoryRepository.findById(id).map(this::toDto);
	}

	@Override
	@Transactional
	public ProductCategoryDto create(ProductCategoryRequest request) {
		ProductCategory category = new ProductCategory();
		category.setName(request.name());
		category.setDescription(request.description());
		return toDto(categoryRepository.save(category));
	}

	@Override
	@Transactional
	public Optional<ProductCategoryDto> update(Long id, ProductCategoryRequest request) {
		return categoryRepository.findById(id)
				.map(category -> {
					category.setName(request.name());
					category.setDescription(request.description());
					return toDto(categoryRepository.save(category));
				});
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		if (!categoryRepository.existsById(id)) {
			return false;
		}
		categoryRepository.deleteById(id);
		return true;
	}

	private ProductCategoryDto toDto(ProductCategory category) {
		return new ProductCategoryDto(category.getId(), category.getName(), category.getDescription());
	}
}
