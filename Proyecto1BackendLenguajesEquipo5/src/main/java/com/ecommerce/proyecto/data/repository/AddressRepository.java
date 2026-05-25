package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.domain.model.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUserId(Long userId);
}
