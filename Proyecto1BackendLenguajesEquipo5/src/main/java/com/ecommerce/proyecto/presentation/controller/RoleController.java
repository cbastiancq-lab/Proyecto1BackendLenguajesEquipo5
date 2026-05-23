package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.RoleService;
import com.ecommerce.proyecto.domain.dto.RoleDto;
import com.ecommerce.proyecto.domain.request.RoleRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping
	public List<RoleDto> findAll() {
		return roleService.findAll();
	}

	@PostMapping
	public ResponseEntity<RoleDto> create(@Valid @RequestBody RoleRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
	}
}
