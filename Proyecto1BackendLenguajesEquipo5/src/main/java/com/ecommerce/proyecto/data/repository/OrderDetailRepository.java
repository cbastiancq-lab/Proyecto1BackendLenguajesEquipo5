package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.OrderDetail;
import com.ecommerce.proyecto.domain.dto.TopProductByCategoryDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	@Query("""
			SELECT new com.ecommerce.proyecto.domain.dto.TopProductByCategoryDto(
				od.product.category.id,
				od.product.category.name,
				od.product.id,
				od.product.name,
				SUM(od.quantity)
			)
			FROM OrderDetail od
			WHERE od.order.createdAt >= :since
			  AND od.order.status <> com.ecommerce.proyecto.domain.enums.OrderStatus.CANCELLED
			GROUP BY od.product.category.id, od.product.category.name,
			         od.product.id, od.product.name
			ORDER BY od.product.category.name ASC, SUM(od.quantity) DESC
			""")
	List<TopProductByCategoryDto> findTopProductsByCategorySince(@Param("since") LocalDateTime since);
}
