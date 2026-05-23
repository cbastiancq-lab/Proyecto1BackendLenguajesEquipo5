package com.ecommerce.proyecto.data.repository;

import com.ecommerce.proyecto.data.entity.ContactInformation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInformationRepository extends JpaRepository<ContactInformation, Long> {

	Optional<ContactInformation> findByUserId(Long userId);
}
