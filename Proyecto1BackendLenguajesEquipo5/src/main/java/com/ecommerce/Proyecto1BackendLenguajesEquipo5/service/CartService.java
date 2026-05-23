package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CartDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.CartRequest;
import com.ecommerce.proyecto.data.entity.Cart;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.CartRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	public CartService(CartRepository cartRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
	}

	public List<CartDto> findAll() {
		return cartRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	public Optional<CartDto> findById(Long id) {
		return cartRepository.findById(id).map(this::toDto);
	}

	public Optional<CartDto> findByUserId(Long userId) {
		return cartRepository.findByUserId(userId).map(this::toDto);
	}

	public CartDto create(CartRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Cart cart = new Cart();
		cart.setUser(user);
		return toDto(cartRepository.save(cart));
	}

	public boolean delete(Long id) {
		if (!cartRepository.existsById(id)) {
			return false;
		}
		cartRepository.deleteById(id);
		return true;
	}

	private CartDto toDto(Cart cart) {
		User user = cart.getUser();
		return new CartDto(cart.getId(), user.getId(), user.getEmail(), cart.getCreatedAt());
	}
}
