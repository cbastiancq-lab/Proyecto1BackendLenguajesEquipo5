package com.ecommerce.Proyecto1BackendLenguajesEquipo5.config;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {
		return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
	}
}
