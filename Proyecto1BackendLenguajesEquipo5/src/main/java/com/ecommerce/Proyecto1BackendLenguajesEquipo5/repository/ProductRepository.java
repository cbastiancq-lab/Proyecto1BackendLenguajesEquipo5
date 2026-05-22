package com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
