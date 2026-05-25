package com.ecommerce.proyecto.domain.dto;

public record TopProductByCategoryDto(
		Long categoryId,
		String categoryName,
		Long productId,
		String productName,
		Long unitsSold
) {
}
