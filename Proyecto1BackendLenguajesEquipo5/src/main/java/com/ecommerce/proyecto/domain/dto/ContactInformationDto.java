package com.ecommerce.proyecto.domain.dto;

public record ContactInformationDto(
		Long id,
		Long userId,
		String phone,
		String secondaryPhone
) {
}
