package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.RoleDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.RoleRequest;
import com.ecommerce.proyecto.data.entity.Role;
import com.ecommerce.proyecto.domain.enums.RoleName;
import com.ecommerce.proyecto.data.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<RoleDto> findAll() {
		return roleRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	public RoleDto create(RoleRequest request) {
		RoleName roleName = RoleName.valueOf(request.name().toUpperCase());
		Role role = roleRepository.findByName(roleName)
				.orElseGet(() -> roleRepository.save(new Role(null, roleName)));
		return toDto(role);
	}

	public Optional<Role> findOrCreate(RoleName roleName) {
		return Optional.of(roleRepository.findByName(roleName)
				.orElseGet(() -> roleRepository.save(new Role(null, roleName))));
	}

	private RoleDto toDto(Role role) {
		return new RoleDto(role.getId(), role.getName().name());
	}
}
