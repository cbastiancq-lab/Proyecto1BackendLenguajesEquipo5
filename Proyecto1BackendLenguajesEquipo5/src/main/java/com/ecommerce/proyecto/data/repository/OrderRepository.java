package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.Order;
import com.ecommerce.proyecto.domain.dto.SalesByCustomerDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);

	@Query("""
			SELECT new com.ecommerce.proyecto.domain.dto.SalesByCustomerDto(
				o.user.id,
				CONCAT(o.user.firstName, ' ', o.user.lastName),
				o.user.email,
				SUM(o.total)
			)
			FROM Order o
			WHERE o.createdAt >= :since
			  AND o.status <> com.ecommerce.proyecto.domain.enums.OrderStatus.CANCELLED
			GROUP BY o.user.id, o.user.firstName, o.user.lastName, o.user.email
			ORDER BY SUM(o.total) DESC
			""")
	List<SalesByCustomerDto> findSalesByCustomerSince(@Param("since") LocalDateTime since);
}
