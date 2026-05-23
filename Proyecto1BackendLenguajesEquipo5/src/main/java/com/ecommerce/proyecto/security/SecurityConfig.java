package com.ecommerce.proyecto.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource))
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("GET", "/api/products/**").permitAll()
				.requestMatchers("GET", "/api/categories/**").permitAll()
				.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception ->
				exception
					.authenticationEntryPoint((request, response, authException) -> {
						response.setStatus(401);
						response.setContentType("application/json");
						response.getWriter().write("{\"error\": \"Unauthorized\"}");
					})
					.accessDeniedHandler((request, response, accessDeniedException) -> {
						response.setStatus(403);
						response.setContentType("application/json");
						response.getWriter().write("{\"error\": \"Forbidden\"}");
					})
			);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
