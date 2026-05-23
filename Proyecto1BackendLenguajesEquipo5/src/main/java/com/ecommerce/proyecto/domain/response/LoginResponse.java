package com.ecommerce.proyecto.domain.response;

public record LoginResponse(
		String token,
		String tokenType
) {
}
