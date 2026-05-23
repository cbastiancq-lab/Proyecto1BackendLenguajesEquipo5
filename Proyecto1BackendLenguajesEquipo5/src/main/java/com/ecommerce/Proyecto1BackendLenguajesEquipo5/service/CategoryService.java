package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CategoryDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CategoryRequest;
import com.ecommerce.proyecto.data.entity.Category;
import com.ecommerce.proyecto.data.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryDto> findAll() {
		return categoryRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	public Optional<CategoryDto> findById(Long id) {
		return categoryRepository.findById(id).map(this::toDto);
	}

	public CategoryDto create(CategoryRequest request) {
		Category category = new Category();
		category.setName(request.name());
		category.setDescription(request.description());
		return toDto(categoryRepository.save(category));
	}

	public Optional<CategoryDto> update(Long id, CategoryRequest request) {
		return categoryRepository.findById(id)
				.map(category -> {
					category.setName(request.name());
					category.setDescription(request.description());
					return toDto(categoryRepository.save(category));
				});
	}

	public boolean delete(Long id) {
		if (!categoryRepository.existsById(id)) {
			return false;
		}
		categoryRepository.deleteById(id);
		return true;
	}

	private CategoryDto toDto(Category category) {
		return new CategoryDto(category.getId(), category.getName(), category.getDescription());
	}
}
