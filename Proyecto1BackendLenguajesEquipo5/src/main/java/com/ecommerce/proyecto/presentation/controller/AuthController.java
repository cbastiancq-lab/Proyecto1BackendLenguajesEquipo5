package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.AuthService;
import com.ecommerce.proyecto.domain.dto.UserDto;
import com.ecommerce.proyecto.domain.request.LoginRequest;
import com.ecommerce.proyecto.domain.request.RegisterRequest;
import com.ecommerce.proyecto.domain.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}
}
