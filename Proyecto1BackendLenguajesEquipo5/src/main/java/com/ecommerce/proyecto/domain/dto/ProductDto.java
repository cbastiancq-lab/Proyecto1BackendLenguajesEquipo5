package com.ecommerce.proyecto.domain.dto;

import java.math.BigDecimal;

public record ProductDto(
		Long id,
		String name,
		String description,
		BigDecimal price,
		Integer stock,
		boolean active,
		Long categoryId,
		String categoryName
) {
}
