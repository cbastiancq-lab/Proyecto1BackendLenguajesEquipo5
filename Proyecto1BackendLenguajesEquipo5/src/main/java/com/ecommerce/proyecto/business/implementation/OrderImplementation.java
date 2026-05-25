package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.OrderService;
import com.ecommerce.proyecto.data.entity.CartItem;
import com.ecommerce.proyecto.data.entity.Order;
import com.ecommerce.proyecto.data.entity.OrderDetail;
import com.ecommerce.proyecto.data.entity.Product;
import com.ecommerce.proyecto.data.entity.ShoppingCart;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.CartItemRepository;
import com.ecommerce.proyecto.data.repository.OrderRepository;
import com.ecommerce.proyecto.data.repository.ProductRepository;
import com.ecommerce.proyecto.data.repository.ShoppingCartRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.dto.OrderDetailDto;
import com.ecommerce.proyecto.domain.enums.OrderStatus;
import com.ecommerce.proyecto.domain.request.OrderDetailRequest;
import com.ecommerce.proyecto.domain.request.OrderRequest;
import com.ecommerce.proyecto.domain.response.OrderResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderImplementation implements OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ShoppingCartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	@Value("${app.business.tax-rate:0.13}")
	private BigDecimal taxRate;

	public OrderImplementation(
			OrderRepository orderRepository,
			UserRepository userRepository,
			ProductRepository productRepository,
			ShoppingCartRepository cartRepository,
			CartItemRepository cartItemRepository
	) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> findAll() {
		return orderRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<OrderResponse> findById(Long id) {
		return orderRepository.findById(id).map(this::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId).stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional
	public OrderResponse create(OrderRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

		Order order = new Order();
		order.setUser(user);
		order.setStatus(resolveStatus(request.status()));

		boolean usingCart = request.details() == null || request.details().isEmpty();
		BigDecimal subtotal = BigDecimal.ZERO;

		if (usingCart) {
			ShoppingCart cart = cartRepository.findByUserId(user.getId())
					.orElseThrow(() -> new IllegalArgumentException("El usuario no tiene carrito"));

			List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
			if (items.isEmpty()) {
				throw new IllegalArgumentException("El carrito está vacío");
			}

			for (CartItem item : items) {
				Product product = item.getProduct();
				validateStock(product, item.getQuantity());

				OrderDetail detail = buildDetail(order, product, item.getQuantity(), product.getPrice());
				order.getDetails().add(detail);
				subtotal = subtotal.add(detail.getSubtotal());

				product.setStock(product.getStock() - item.getQuantity());
				productRepository.save(product);
			}

			cartItemRepository.deleteByCartId(cart.getId());
		} else {
			for (OrderDetailRequest detailRequest : request.details()) {
				Product product = productRepository.findById(detailRequest.productId())
						.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + detailRequest.productId()));
				validateStock(product, detailRequest.quantity());

				BigDecimal unitPrice = detailRequest.unitPrice() != null
						? detailRequest.unitPrice()
						: product.getPrice();

				OrderDetail detail = buildDetail(order, product, detailRequest.quantity(), unitPrice);
				order.getDetails().add(detail);
				subtotal = subtotal.add(detail.getSubtotal());

				product.setStock(product.getStock() - detailRequest.quantity());
				productRepository.save(product);
			}
		}

		BigDecimal tax = subtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
		BigDecimal total = subtotal.add(tax).setScale(2, RoundingMode.HALF_UP);
		order.setTotal(total);

		return toResponse(orderRepository.save(order));
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		if (!orderRepository.existsById(id)) {
			return false;
		}
		orderRepository.deleteById(id);
		return true;
	}

	private void validateStock(Product product, int quantity) {
		if (!product.isActive()) {
			throw new IllegalArgumentException("Producto inactivo: " + product.getName());
		}
		if (product.getStock() < quantity) {
			throw new IllegalArgumentException(
					"Stock insuficiente para " + product.getName()
							+ ". Disponible: " + product.getStock() + ", solicitado: " + quantity
			);
		}
	}

	private OrderDetail buildDetail(Order order, Product product, int quantity, BigDecimal unitPrice) {
		OrderDetail detail = new OrderDetail();
		detail.setOrder(order);
		detail.setProduct(product);
		detail.setQuantity(quantity);
		detail.setUnitPrice(unitPrice);
		detail.setSubtotal(unitPrice.multiply(BigDecimal.valueOf(quantity)));
		return detail;
	}

	private OrderStatus resolveStatus(String status) {
		if (status == null || status.isBlank()) {
			return OrderStatus.CONFIRMED;
		}
		return OrderStatus.valueOf(status.toUpperCase());
	}

	private OrderResponse toResponse(Order order) {
		User user = order.getUser();
		List<OrderDetailDto> details = order.getDetails().stream()
				.map(this::toDetailDto)
				.toList();

		return new OrderResponse(
				order.getId(),
				user.getId(),
				user.getEmail(),
				order.getTotal(),
				order.getStatus().name(),
				order.getCreatedAt(),
				details
		);
	}

	private OrderDetailDto toDetailDto(OrderDetail detail) {
		Product product = detail.getProduct();
		return new OrderDetailDto(
				detail.getId(),
				product.getId(),
				product.getName(),
				detail.getQuantity(),
				detail.getUnitPrice(),
				detail.getSubtotal()
		);
	}
}
