package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUserId(Long userId);
}
