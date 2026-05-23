package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);
}
