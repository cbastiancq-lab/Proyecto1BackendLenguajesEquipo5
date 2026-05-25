package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.domain.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
