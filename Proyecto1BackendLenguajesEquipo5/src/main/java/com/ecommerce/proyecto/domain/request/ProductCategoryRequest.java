package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductCategoryRequest(
		@NotBlank String name,
		@Size(max = 500) String description
) {
}
