package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
		@NotNull Long userId,
		@NotBlank String province,
		@NotBlank String city,
		String district,
		@NotBlank String details,
		Boolean mainAddress
) {
}
