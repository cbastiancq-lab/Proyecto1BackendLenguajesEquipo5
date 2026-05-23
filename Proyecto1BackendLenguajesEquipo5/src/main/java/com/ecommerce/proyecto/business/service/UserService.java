package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.UserDto;
import com.ecommerce.proyecto.domain.request.RegisterRequest;
import com.ecommerce.proyecto.domain.request.UserRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {

	List<UserDto> findAll();

	Optional<UserDto> findById(Long id);

	UserDto create(UserRequest request);

	UserDto registerClient(RegisterRequest request);

	boolean delete(Long id);
}
