package com.ecommerce.Proyecto1BackendLenguajesEquipo5.repository;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
