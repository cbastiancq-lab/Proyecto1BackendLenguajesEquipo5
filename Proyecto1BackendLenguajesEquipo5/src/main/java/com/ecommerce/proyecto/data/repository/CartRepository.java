package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUserId(Long userId);
}
