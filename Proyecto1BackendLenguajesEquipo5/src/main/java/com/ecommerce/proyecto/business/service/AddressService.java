package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.AddressDto;
import com.ecommerce.proyecto.domain.request.AddressRequest;
import java.util.List;
import java.util.Optional;

public interface AddressService {

	List<AddressDto> findAll();

	List<AddressDto> findByUserId(Long userId);

	Optional<AddressDto> findById(Long id);

	AddressDto create(AddressRequest request);

	boolean delete(Long id);
}
