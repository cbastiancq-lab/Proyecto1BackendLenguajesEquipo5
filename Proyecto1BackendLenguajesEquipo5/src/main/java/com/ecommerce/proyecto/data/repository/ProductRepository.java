package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
