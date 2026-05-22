package com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.Role;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);

	boolean existsByName(RoleName name);
}
