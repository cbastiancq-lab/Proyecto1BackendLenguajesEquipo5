package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record OrderRequest(
		@NotNull Long userId,
		@NotNull @PositiveOrZero BigDecimal total,
		String status
) {
}
