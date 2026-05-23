package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
		@NotBlank String name,
		@Size(max = 1000) String description,
		@NotNull @PositiveOrZero BigDecimal price,
		@NotNull @PositiveOrZero Integer stock,
		Boolean active,
		Long categoryId
) {
}
