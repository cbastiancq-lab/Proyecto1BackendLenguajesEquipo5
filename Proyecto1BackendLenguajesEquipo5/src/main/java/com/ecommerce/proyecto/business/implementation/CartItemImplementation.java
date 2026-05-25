package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.CartItemService;
import com.ecommerce.proyecto.data.entity.CartItem;
import com.ecommerce.proyecto.data.entity.Product;
import com.ecommerce.proyecto.data.entity.ShoppingCart;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.CartItemRepository;
import com.ecommerce.proyecto.data.repository.ProductRepository;
import com.ecommerce.proyecto.data.repository.ShoppingCartRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.dto.CartItemDto;
import com.ecommerce.proyecto.domain.request.CartItemRequest;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemImplementation implements CartItemService {

	private final CartItemRepository cartItemRepository;
	private final ShoppingCartRepository cartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public CartItemImplementation(
			CartItemRepository cartItemRepository,
			ShoppingCartRepository cartRepository,
			ProductRepository productRepository,
			UserRepository userRepository
	) {
		this.cartItemRepository = cartItemRepository;
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartItemDto> findByCartId(Long cartId) {
		return cartItemRepository.findByCartId(cartId).stream()
				.map(this::toDto)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartItemDto> findByUserId(Long userId) {
		ShoppingCart cart = cartRepository.findByUserId(userId).orElse(null);
		if (cart == null) {
			return List.of();
		}
		return findByCartId(cart.getId());
	}

	@Override
	@Transactional
	public CartItemDto addItem(Long userId, CartItemRequest request) {
		ShoppingCart cart = getOrCreateCart(userId);
		Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

		if (!product.isActive()) {
			throw new IllegalArgumentException("El producto no está disponible");
		}

		CartItem item = cartItemRepository
				.findByCartIdAndProductId(cart.getId(), product.getId())
				.orElseGet(() -> {
					CartItem newItem = new CartItem();
					newItem.setCart(cart);
					newItem.setProduct(product);
					newItem.setQuantity(0);
					return newItem;
				});

		int newQuantity = item.getQuantity() + request.quantity();
		if (newQuantity > product.getStock()) {
			throw new IllegalArgumentException(
					"Stock insuficiente. Disponible: " + product.getStock()
			);
		}

		item.setQuantity(newQuantity);
		return toDto(cartItemRepository.save(item));
	}

	@Override
	@Transactional
	public CartItemDto updateQuantity(Long userId, Long productId, Integer quantity) {
		if (quantity == null || quantity < 1) {
			throw new IllegalArgumentException("La cantidad debe ser al menos 1");
		}

		ShoppingCart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

		CartItem item = cartItemRepository
				.findByCartIdAndProductId(cart.getId(), productId)
				.orElseThrow(() -> new IllegalArgumentException("Producto no está en el carrito"));

		Product product = item.getProduct();
		if (quantity > product.getStock()) {
			throw new IllegalArgumentException(
					"Stock insuficiente. Disponible: " + product.getStock()
			);
		}

		item.setQuantity(quantity);
		return toDto(cartItemRepository.save(item));
	}

	@Override
	@Transactional
	public boolean removeItem(Long userId, Long productId) {
		ShoppingCart cart = cartRepository.findByUserId(userId).orElse(null);
		if (cart == null) {
			return false;
		}
		return cartItemRepository
				.findByCartIdAndProductId(cart.getId(), productId)
				.map(item -> {
					cartItemRepository.delete(item);
					return true;
				})
				.orElse(false);
	}

	@Override
	@Transactional
	public void clearCart(Long userId) {
		cartRepository.findByUserId(userId).ifPresent(cart ->
				cartItemRepository.deleteByCartId(cart.getId())
		);
	}

	private ShoppingCart getOrCreateCart(Long userId) {
		return cartRepository.findByUserId(userId).orElseGet(() -> {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
			ShoppingCart cart = new ShoppingCart();
			cart.setUser(user);
			return cartRepository.save(cart);
		});
	}

	private CartItemDto toDto(CartItem item) {
		Product product = item.getProduct();
		BigDecimal subtotal = product.getPrice()
				.multiply(BigDecimal.valueOf(item.getQuantity()));
		return new CartItemDto(
				item.getId(),
				product.getId(),
				product.getName(),
				item.getQuantity(),
				product.getPrice(),
				subtotal,
				product.getStock()
		);
	}
}
