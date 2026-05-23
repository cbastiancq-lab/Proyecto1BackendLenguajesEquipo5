package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.UserDto;
import com.ecommerce.proyecto.domain.request.LoginRequest;
import com.ecommerce.proyecto.domain.request.RegisterRequest;
import com.ecommerce.proyecto.domain.response.LoginResponse;

public interface AuthService {

	LoginResponse login(LoginRequest request);

	UserDto register(RegisterRequest request);
}
