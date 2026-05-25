package com.ecommerce.proyecto.domain.dto;

import java.math.BigDecimal;

public record SalesByCustomerDto(
		Long userId,
		String customerName,
		String email,
		BigDecimal totalPurchased
) {
}
