package com.ecommerce.proyecto.exception;

import com.ecommerce.proyecto.domain.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
	}
}
