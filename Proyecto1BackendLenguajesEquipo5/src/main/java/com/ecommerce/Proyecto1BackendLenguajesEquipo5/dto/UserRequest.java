package com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record UserRequest(
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank @Email String email,
		@NotBlank @Size(min = 8) String password,
		Set<String> roles
) {
}
