package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCartId(Long cartId);

	Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

	@Modifying
	@Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
	void deleteByCartId(@Param("cartId") Long cartId);
}
