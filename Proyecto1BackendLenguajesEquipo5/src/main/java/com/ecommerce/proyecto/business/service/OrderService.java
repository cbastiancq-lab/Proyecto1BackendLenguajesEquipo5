package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.request.OrderRequest;
import com.ecommerce.proyecto.domain.response.OrderResponse;
import java.util.List;
import java.util.Optional;

public interface OrderService {

	List<OrderResponse> findAll();

	Optional<OrderResponse> findById(Long id);

	List<OrderResponse> findByUserId(Long userId);

	OrderResponse create(OrderRequest request);

	boolean delete(Long id);
}
