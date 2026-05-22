package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

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
