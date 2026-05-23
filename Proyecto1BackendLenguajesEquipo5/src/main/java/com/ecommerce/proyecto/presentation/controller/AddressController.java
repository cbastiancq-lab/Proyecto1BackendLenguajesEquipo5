package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.AddressService;
import com.ecommerce.proyecto.domain.dto.AddressDto;
import com.ecommerce.proyecto.domain.request.AddressRequest;
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
@RequestMapping("/api/addresses")
public class AddressController {

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping
	public List<AddressDto> findAll() {
		return addressService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AddressDto> findById(@PathVariable Long id) {
		return addressService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/user/{userId}")
	public List<AddressDto> findByUserId(@PathVariable Long userId) {
		return addressService.findByUserId(userId);
	}

	@PostMapping
	public ResponseEntity<AddressDto> create(@Valid @RequestBody AddressRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return addressService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
