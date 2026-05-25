package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.domain.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
