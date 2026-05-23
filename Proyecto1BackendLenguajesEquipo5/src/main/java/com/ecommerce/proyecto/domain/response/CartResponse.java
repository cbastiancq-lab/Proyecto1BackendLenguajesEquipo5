package com.ecommerce.proyecto.domain.response;

import java.time.LocalDateTime;

public record CartResponse(
		Long id,
		Long userId,
		String userEmail,
		LocalDateTime createdAt
) {
}
