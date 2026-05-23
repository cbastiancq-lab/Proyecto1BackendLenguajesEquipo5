package com.ecommerce.Proyecto1BackendLenguajesEquipo5.service;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.RegisterRequest;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.UserDto;
import com.ecommerce.Proyecto1BackendLenguajesEquipo5.dto.UserRequest;
import com.ecommerce.proyecto.data.entity.Role;
import com.ecommerce.proyecto.domain.enums.RoleName;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.RoleRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<UserDto> findAll() {
		return userRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	public Optional<UserDto> findById(Long id) {
		return userRepository.findById(id).map(this::toDto);
	}

	public UserDto create(UserRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already exists");
		}

		User user = new User();
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setEnabled(true);
		user.setRoles(resolveRoles(request.roles()));

		return toDto(userRepository.save(user));
	}

	public UserDto registerClient(RegisterRequest request) {
		UserRequest userRequest = new UserRequest(
				request.firstName(),
				request.lastName(),
				request.email(),
				request.password(),
				Set.of(RoleName.CLIENT.name())
		);
		return create(userRequest);
	}

	public boolean delete(Long id) {
		if (!userRepository.existsById(id)) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}

	private Set<Role> resolveRoles(Set<String> requestedRoles) {
		Set<String> roleNames = requestedRoles == null || requestedRoles.isEmpty()
				? Set.of(RoleName.CLIENT.name())
				: requestedRoles;

		return roleNames.stream()
				.map(roleName -> RoleName.valueOf(roleName.toUpperCase()))
				.map(this::findOrCreateRole)
				.collect(Collectors.toCollection(HashSet::new));
	}

	private Role findOrCreateRole(RoleName roleName) {
		return roleRepository.findByName(roleName)
				.orElseGet(() -> roleRepository.save(new Role(null, roleName)));
	}

	private UserDto toDto(User user) {
		Set<String> roles = user.getRoles().stream()
				.map(role -> role.getName().name())
				.collect(Collectors.toSet());

		return new UserDto(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.isEnabled(),
				roles
		);
	}
}
