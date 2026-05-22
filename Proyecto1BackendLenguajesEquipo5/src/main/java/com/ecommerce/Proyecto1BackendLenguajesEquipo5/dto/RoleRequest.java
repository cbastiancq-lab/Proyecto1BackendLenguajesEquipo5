package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
		@NotBlank String name
) {
}
