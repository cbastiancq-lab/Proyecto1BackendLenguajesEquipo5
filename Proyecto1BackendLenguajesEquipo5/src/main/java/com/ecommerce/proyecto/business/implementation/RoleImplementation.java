package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.RoleService;
import com.ecommerce.proyecto.domain.model.Role;
import com.ecommerce.proyecto.data.repository.RoleRepository;
import com.ecommerce.proyecto.domain.dto.RoleDto;
import com.ecommerce.proyecto.domain.enums.RoleName;
import com.ecommerce.proyecto.domain.request.RoleRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleImplementation implements RoleService {

	private final RoleRepository roleRepository;

	public RoleImplementation(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoleDto> findAll() {
		return roleRepository.findAll().stream().map(this::toDto).toList();
	}

	@Override
	@Transactional
	public RoleDto create(RoleRequest request) {
		RoleName roleName = RoleName.valueOf(request.name().toUpperCase());
		Role role = roleRepository.findByName(roleName)
				.orElseGet(() -> roleRepository.save(new Role(null, roleName)));
		return toDto(role);
	}

	@Override
	@Transactional
	public void ensureRole(RoleName roleName) {
		if (!roleRepository.existsByName(roleName)) {
			roleRepository.save(new Role(null, roleName));
		}
	}

	private RoleDto toDto(Role role) {
		return new RoleDto(role.getId(), role.getName().name());
	}
}
