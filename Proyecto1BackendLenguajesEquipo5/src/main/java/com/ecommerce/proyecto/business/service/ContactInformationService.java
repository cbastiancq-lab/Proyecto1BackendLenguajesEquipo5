package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.ContactInformationDto;
import com.ecommerce.proyecto.domain.request.ContactInformationRequest;
import java.util.List;
import java.util.Optional;

public interface ContactInformationService {

	List<ContactInformationDto> findAll();

	Optional<ContactInformationDto> findById(Long id);

	Optional<ContactInformationDto> findByUserId(Long userId);

	ContactInformationDto create(ContactInformationRequest request);

	boolean delete(Long id);
}
