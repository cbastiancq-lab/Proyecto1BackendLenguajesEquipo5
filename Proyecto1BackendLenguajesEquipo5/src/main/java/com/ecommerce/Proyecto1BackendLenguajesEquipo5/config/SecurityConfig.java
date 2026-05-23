package com.ecommerce.Proyecto1BackendLenguajesEquipo5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuración moderna de Spring Security 6.x usando SecurityFilterChain.
 * 
 * Esta configuración implementa:
 * - CSRF deshabilitado (para APIs REST stateless)
 * - CORS habilitado desde el bean CorsConfigurationSource
 * - Sesiones STATELESS (JWT compatible)
 * - Rutas públicas (/api/auth/**, /api/products/**, /api/categories/**)
 * - Rutas protegidas (resto de endpoints)
 * - BCryptPasswordEncoder para haseo seguro de contraseñas
 */
@Configuration
public class SecurityConfig {

	/**
	 * Configura el filtro de seguridad de la aplicación.
	 * 
	 * Este es el punto central donde se definen todas las reglas de seguridad.
	 * Cada endpoint se configura con su política de acceso específica.
	 * 
	 * @param http el objeto HttpSecurity que configura la seguridad HTTP
	 * @param corsConfigurationSource el bean de CORS inyectado desde CorsConfig
	 * @return el SecurityFilterChain configurado
	 * @throws Exception si ocurre error en la configuración
	 */
	@Bean
	public SecurityFilterChain filterChain(
			HttpSecurity http,
			CorsConfigurationSource corsConfigurationSource) throws Exception {

		// 1. Configurar CORS
		// Usa el CorsConfigurationSource definido en CorsConfig
		// Esto permite solicitudes desde http://localhost:4200
		http.cors(cors -> cors.configurationSource(corsConfigurationSource));

		// 2. CSRF Protection - Deshabilitado para APIs REST stateless
		// Las APIs REST con tokens JWT no necesitan CSRF protection
		// El token JWT actúa como validador de origen
		http.csrf(csrf -> csrf.disable());

		// 3. Configurar autorización de endpoints
		http.authorizeHttpRequests(authz -> authz
				// PÚBLICOS - Sin autenticación requerida
				// Endpoints de autenticación (login, register, etc.)
				.requestMatchers("/api/auth/**").permitAll()
				
				// Endpoints de productos - solo GET permitido públicamente
				.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
				
				// Endpoints de categorías - solo GET permitido públicamente
				.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
				
				// PROTEGIDOS - Requieren autenticación
				// Cualquier otra solicitud debe estar autenticada
				.anyRequest().authenticated()
		);

		// 4. Configurar sesiones STATELESS
		// Indica a Spring Security que no cree sesiones HTTP (HttpSession)
		// Importante para aplicaciones con JWT o token-based authentication
		// Cada request es independiente y se autentica por token
		http.sessionManagement(session -> 
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		// 5. Configurar manejo de excepciones de autenticación
		// Para APIs REST, es mejor retornar 401 que redirigir a login page
		http.exceptionHandling(exception ->
			exception
				// Sin autenticación (no incluye token o token inválido)
				.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(401);
					response.setContentType("application/json");
					response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" 
						+ authException.getMessage() + "\"}");
				})
				// Sin autorización (autenticado pero sin permisos)
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setStatus(403);
					response.setContentType("application/json");
					response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"" 
						+ accessDeniedException.getMessage() + "\"}");
				})
		);

		return http.build();
	}

	/**
	 * Bean para el PasswordEncoder.
	 * 
	 * BCryptPasswordEncoder utiliza el algoritmo bcrypt con un salt aleatorio
	 * para hashear contraseñas de manera segura.
	 * 
	 * Características:
	 * - Usa salt aleatorio (imposible obtener contraseña original)
	 * - Adaptativo: más lento en el futuro si se mejora el hardware
	 * - Estándar de la industria para almacenar contraseñas
	 * - Compatible con Spring Security UserDetailsService
	 * 
	 * Uso:
	 * @Autowired
	 * private PasswordEncoder passwordEncoder;
	 * 
	 * // Para hashear contraseña en registro
	 * String hashedPassword = passwordEncoder.encode(plainPassword);
	 * 
	 * // Para verificar en login
	 * boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);
	 * 
	 * @return instancia de BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Bean para el AuthenticationManager.
	 * 
	 * Este bean es necesario para autenticar usuarios programáticamente
	 * en controllers (e.g., en un endpoint de login).
	 * 
	 * Uso en un AuthController:
	 * @Autowired
	 * private AuthenticationManager authenticationManager;
	 * 
	 * Authentication auth = authenticationManager.authenticate(
	 * 		new UsernamePasswordAuthenticationToken(username, password)
	 * );
	 * 
	 * @param authenticationConfiguration la configuración de autenticación
	 * @return el AuthenticationManager
	 * @throws Exception si ocurre error
	 */
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
