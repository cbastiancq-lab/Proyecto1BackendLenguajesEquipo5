package com.ecommerce.proyecto.business.service;

import com.ecommerce.proyecto.domain.dto.RoleDto;
import com.ecommerce.proyecto.domain.enums.RoleName;
import com.ecommerce.proyecto.domain.request.RoleRequest;
import java.util.List;

public interface RoleService {

	List<RoleDto> findAll();

	RoleDto create(RoleRequest request);

	void ensureRole(RoleName roleName);
}
