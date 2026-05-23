package com.ecommerce.proyecto.domain.dto;

import java.math.BigDecimal;

public record OrderDetailDto(
		Long id,
		Long productId,
		String productName,
		Integer quantity,
		BigDecimal unitPrice,
		BigDecimal subtotal
) {
}
