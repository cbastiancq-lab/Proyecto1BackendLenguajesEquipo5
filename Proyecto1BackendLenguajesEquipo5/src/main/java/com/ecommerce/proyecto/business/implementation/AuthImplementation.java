package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.AuthService;
import com.ecommerce.proyecto.business.service.UserService;
import com.ecommerce.proyecto.domain.dto.UserDto;
import com.ecommerce.proyecto.domain.request.LoginRequest;
import com.ecommerce.proyecto.domain.request.RegisterRequest;
import com.ecommerce.proyecto.domain.response.LoginResponse;
import com.ecommerce.proyecto.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthImplementation implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	private final UserService userService;

	public AuthImplementation(
			AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService,
			JwtService jwtService,
			UserService userService
	) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		authenticationManager.authenticate(
				UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password())
		);
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
		return new LoginResponse(jwtService.generateToken(userDetails), "Bearer");
	}

	@Override
	public UserDto register(RegisterRequest request) {
		return userService.registerClient(request);
	}
}
