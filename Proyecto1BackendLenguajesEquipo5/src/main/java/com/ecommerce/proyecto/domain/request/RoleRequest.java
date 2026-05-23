package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
		@NotBlank String name
) {
}
