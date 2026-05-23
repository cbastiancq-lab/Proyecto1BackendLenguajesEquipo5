package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
