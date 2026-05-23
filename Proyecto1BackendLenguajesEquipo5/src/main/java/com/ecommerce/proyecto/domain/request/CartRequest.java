package com.ecommerce.proyecto.domain.request;

import jakarta.validation.constraints.NotNull;

public record CartRequest(
		@NotNull Long userId
) {
}
