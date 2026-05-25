package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.CartItemDto;
import com.ecommerce.proyecto.domain.request.CartItemRequest;
import java.util.List;

public interface CartItemService {

	List<CartItemDto> findByCartId(Long cartId);

	List<CartItemDto> findByUserId(Long userId);

	CartItemDto addItem(Long userId, CartItemRequest request);

	CartItemDto updateQuantity(Long userId, Long productId, Integer quantity);

	boolean removeItem(Long userId, Long productId);

	void clearCart(Long userId);
}
