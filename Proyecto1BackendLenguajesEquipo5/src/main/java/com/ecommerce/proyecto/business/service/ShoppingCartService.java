package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.request.CartRequest;
import com.ecommerce.proyecto.domain.response.CartResponse;
import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {

	List<CartResponse> findAll();

	Optional<CartResponse> findById(Long id);

	Optional<CartResponse> findByUserId(Long userId);

	CartResponse create(CartRequest request);

	boolean delete(Long id);
}
