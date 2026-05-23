package com.ecommerce.proyecto.domain.dto;

import java.util.Set;

public record UserDto(
		Long id,
		String firstName,
		String lastName,
		String email,
		boolean enabled,
		Set<String> roles
) {
}
