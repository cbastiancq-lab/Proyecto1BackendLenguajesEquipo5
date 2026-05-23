package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactInformationRequest(
		@NotNull Long userId,
		@NotBlank String phone,
		String secondaryPhone
) {
}
