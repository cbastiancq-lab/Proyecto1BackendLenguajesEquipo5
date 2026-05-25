package com.ecommerce.proyecto.domain.dto;

import java.math.BigDecimal;

public record CartItemDto(
		Long id,
		Long productId,
		String productName,
		Integer quantity,
		BigDecimal unitPrice,
		BigDecimal subtotal,
		Integer stockAvailable
) {
}
