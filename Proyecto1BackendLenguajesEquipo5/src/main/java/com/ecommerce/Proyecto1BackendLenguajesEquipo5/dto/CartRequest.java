package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import jakarta.validation.constraints.NotNull;

public record CartRequest(
		@NotNull Long userId
) {
}
