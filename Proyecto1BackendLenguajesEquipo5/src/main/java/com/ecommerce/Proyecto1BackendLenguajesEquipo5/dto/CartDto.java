package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import java.time.LocalDateTime;

public record CartDto(
		Long id,
		Long userId,
		String userEmail,
		LocalDateTime createdAt
) {
}
