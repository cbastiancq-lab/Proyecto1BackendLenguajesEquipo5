package com.ecommerce.proyecto.domain.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
		@NotNull Long userId,
		@NotNull @PositiveOrZero BigDecimal total,
		String status,
		List<@Valid OrderDetailRequest> details
) {
}
