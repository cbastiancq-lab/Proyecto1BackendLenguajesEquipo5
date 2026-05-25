package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.domain.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

	Optional<ShoppingCart> findByUserId(Long userId);
}
