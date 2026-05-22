package com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);
}
