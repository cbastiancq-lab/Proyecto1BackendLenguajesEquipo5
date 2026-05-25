package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.domain.model.Role;
import com.ecommerce.proyecto.domain.enums.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);

	boolean existsByName(RoleName name);
}
