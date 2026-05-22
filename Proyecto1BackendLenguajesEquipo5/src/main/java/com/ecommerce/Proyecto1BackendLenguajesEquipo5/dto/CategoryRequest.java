package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
		@NotBlank String name,
		@Size(max = 500) String description
) {
}
