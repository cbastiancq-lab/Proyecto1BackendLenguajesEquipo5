package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
