package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.AuthRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.AuthResponse;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.RegisterRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.UserDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	private final UserService userService;

	public AuthService(
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

	public AuthResponse login(AuthRequest request) {
		authenticationManager.authenticate(
				UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password())
		);
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
		return new AuthResponse(jwtService.generateToken(userDetails), "Bearer");
	}

	public UserDto register(RegisterRequest request) {
		return userService.registerClient(request);
	}
}
