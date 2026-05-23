package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.ShoppingCartService;
import com.ecommerce.proyecto.data.entity.ShoppingCart;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.ShoppingCartRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.request.CartRequest;
import com.ecommerce.proyecto.domain.response.CartResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartImplementation implements ShoppingCartService {

	private final ShoppingCartRepository cartRepository;
	private final UserRepository userRepository;

	public ShoppingCartImplementation(ShoppingCartRepository cartRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartResponse> findAll() {
		return cartRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CartResponse> findById(Long id) {
		return cartRepository.findById(id).map(this::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CartResponse> findByUserId(Long userId) {
		return cartRepository.findByUserId(userId).map(this::toResponse);
	}

	@Override
	@Transactional
	public CartResponse create(CartRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		ShoppingCart cart = new ShoppingCart();
		cart.setUser(user);
		return toResponse(cartRepository.save(cart));
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		if (!cartRepository.existsById(id)) {
			return false;
		}
		cartRepository.deleteById(id);
		return true;
	}

	private CartResponse toResponse(ShoppingCart cart) {
		User user = cart.getUser();
		return new CartResponse(cart.getId(), user.getId(), user.getEmail(), cart.getCreatedAt());
	}
}
