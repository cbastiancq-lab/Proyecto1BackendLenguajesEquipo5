package com.ecommerce.Proyecto1BackendLenguajesEquipo5.controller;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.UserDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.UserRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<UserDto> findAll() {
		return userService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> findById(@PathVariable Long id) {
		return userService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<UserDto> create(@Valid @RequestBody UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return userService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
