package com.ecommerce.proyecto.domain.response;

import com.ecommerce.proyecto.domain.dto.OrderDetailDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
		Long id,
		Long userId,
		String userEmail,
		BigDecimal total,
		String status,
		LocalDateTime createdAt,
		List<OrderDetailDto> details
) {
}
