package com.ecommerce.proyecto.domain.dto;

public record AddressDto(
		Long id,
		Long userId,
		String province,
		String city,
		String district,
		String details,
		boolean mainAddress
) {
}
