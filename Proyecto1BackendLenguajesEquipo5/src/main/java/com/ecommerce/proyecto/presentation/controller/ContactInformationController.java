package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.ContactInformationService;
import com.ecommerce.proyecto.domain.dto.ContactInformationDto;
import com.ecommerce.proyecto.domain.request.ContactInformationRequest;
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
@RequestMapping("/api/contact-information")
public class ContactInformationController {

	private final ContactInformationService contactService;

	public ContactInformationController(ContactInformationService contactService) {
		this.contactService = contactService;
	}

	@GetMapping
	public List<ContactInformationDto> findAll() {
		return contactService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ContactInformationDto> findById(@PathVariable Long id) {
		return contactService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ContactInformationDto> findByUserId(@PathVariable Long userId) {
		return contactService.findByUserId(userId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ContactInformationDto> create(@Valid @RequestBody ContactInformationRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(contactService.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return contactService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
