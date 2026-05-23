package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.OrderService;
import com.ecommerce.proyecto.data.entity.Order;
import com.ecommerce.proyecto.data.entity.OrderDetail;
import com.ecommerce.proyecto.data.entity.Product;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.OrderRepository;
import com.ecommerce.proyecto.data.repository.ProductRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.dto.OrderDetailDto;
import com.ecommerce.proyecto.domain.enums.OrderStatus;
import com.ecommerce.proyecto.domain.request.OrderDetailRequest;
import com.ecommerce.proyecto.domain.request.OrderRequest;
import com.ecommerce.proyecto.domain.response.OrderResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderImplementation implements OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public OrderImplementation(
			OrderRepository orderRepository,
			UserRepository userRepository,
			ProductRepository productRepository
	) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
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
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Order order = new Order();
		order.setUser(user);
		order.setTotal(request.total());
		order.setStatus(resolveStatus(request.status()));

		if (request.details() != null) {
			request.details().forEach(detailRequest -> order.getDetails().add(toDetail(order, detailRequest)));
		}

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

	private OrderDetail toDetail(Order order, OrderDetailRequest request) {
		Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		OrderDetail detail = new OrderDetail();
		detail.setOrder(order);
		detail.setProduct(product);
		detail.setQuantity(request.quantity());
		detail.setUnitPrice(request.unitPrice());
		detail.setSubtotal(request.unitPrice().multiply(BigDecimal.valueOf(request.quantity())));
		return detail;
	}

	private OrderStatus resolveStatus(String status) {
		if (status == null || status.isBlank()) {
			return OrderStatus.PENDING;
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
