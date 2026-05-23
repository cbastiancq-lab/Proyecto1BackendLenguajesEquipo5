package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.ProductDto;
import com.ecommerce.proyecto.domain.request.ProductRequest;
import java.util.List;
import java.util.Optional;

public interface ProductService {

	List<ProductDto> findAll();

	Optional<ProductDto> findById(Long id);

	ProductDto create(ProductRequest request);

	Optional<ProductDto> update(Long id, ProductRequest request);

	boolean delete(Long id);
}
