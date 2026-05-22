package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.OrderDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.OrderRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.Order;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.OrderStatus;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.User;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository.OrderRepository;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
	}

	public List<OrderDto> findAll() {
		return orderRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	public Optional<OrderDto> findById(Long id) {
		return orderRepository.findById(id).map(this::toDto);
	}

	public List<OrderDto> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId).stream()
				.map(this::toDto)
				.toList();
	}

	public OrderDto create(OrderRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Order order = new Order();
		order.setUser(user);
		order.setTotal(request.total());
		order.setStatus(resolveStatus(request.status()));
		return toDto(orderRepository.save(order));
	}

	public boolean delete(Long id) {
		if (!orderRepository.existsById(id)) {
			return false;
		}
		orderRepository.deleteById(id);
		return true;
	}

	private OrderStatus resolveStatus(String status) {
		if (status == null || status.isBlank()) {
			return OrderStatus.PENDING;
		}
		return OrderStatus.valueOf(status.toUpperCase());
	}

	private OrderDto toDto(Order order) {
		User user = order.getUser();
		return new OrderDto(
				order.getId(),
				user.getId(),
				user.getEmail(),
				order.getTotal(),
				order.getStatus().name(),
				order.getCreatedAt()
		);
	}
}
