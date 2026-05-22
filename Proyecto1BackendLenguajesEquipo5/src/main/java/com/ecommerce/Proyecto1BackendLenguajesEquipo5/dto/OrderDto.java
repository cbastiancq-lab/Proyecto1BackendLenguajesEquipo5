package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
		Long id,
		Long userId,
		String userEmail,
		BigDecimal total,
		String status,
		LocalDateTime createdAt
) {
}
