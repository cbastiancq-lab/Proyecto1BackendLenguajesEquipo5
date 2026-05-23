package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.ProductCategoryDto;
import com.ecommerce.proyecto.domain.request.ProductCategoryRequest;
import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {

	List<ProductCategoryDto> findAll();

	Optional<ProductCategoryDto> findById(Long id);

	ProductCategoryDto create(ProductCategoryRequest request);

	Optional<ProductCategoryDto> update(Long id, ProductCategoryRequest request);

	boolean delete(Long id);
}
