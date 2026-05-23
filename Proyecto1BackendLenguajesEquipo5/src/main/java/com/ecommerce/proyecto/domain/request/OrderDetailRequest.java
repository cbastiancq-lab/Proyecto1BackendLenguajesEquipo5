package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record OrderDetailRequest(
		@NotNull Long productId,
		@NotNull @Positive Integer quantity,
		@NotNull @PositiveOrZero BigDecimal unitPrice
) {
}
