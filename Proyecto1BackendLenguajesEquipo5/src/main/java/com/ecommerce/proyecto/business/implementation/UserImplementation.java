package com.ecommerce.proyecto.business.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.proyecto.business.service.UserService;
import com.ecommerce.proyecto.domain.model.Role;
import com.ecommerce.proyecto.domain.model.User;
import com.ecommerce.proyecto.data.repository.RoleRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.dto.UserDto;
import com.ecommerce.proyecto.domain.enums.RoleName;
import com.ecommerce.proyecto.domain.request.RegisterRequest;
import com.ecommerce.proyecto.domain.request.UserRequest;

@Service
public class UserImplementation implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserImplementation(
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> findAll() {
		return userRepository.findAll().stream().map(this::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> findById(Long id) {
		return userRepository.findById(id).map(this::toDto);
	}

	@Override
	@Transactional
	public UserDto create(UserRequest request) {

		System.out.println("CREATE USER");

		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already exists");
		}

		User user = new User();

		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());

		System.out.println("ANTES DE ENCODE PASSWORD");

		user.setPassword(passwordEncoder.encode(request.password()));

		System.out.println("ANTES DE ROLES");

		user.setEnabled(true);
		user.setRoles(resolveRoles(request.roles()));

		System.out.println("ANTES DE SAVE");

		User savedUser = userRepository.save(user);

		System.out.println("USER GUARDADO");

		return toDto(savedUser);
	}

	@Override
	public UserDto registerClient(RegisterRequest request) {

		System.out.println("REGISTER CLIENT");

		UserRequest userRequest = new UserRequest(
				request.firstName(),
				request.lastName(),
				request.email(),
				request.password(),
				Set.of(RoleName.CLIENT.name())
		);

		return create(userRequest);
	}

	@Override
	@Transactional
	public boolean delete(Long id) {

		if (!userRepository.existsById(id)) {
			return false;
		}

		userRepository.deleteById(id);

		return true;
	}

	private Set<Role> resolveRoles(Set<String> requestedRoles) {

		System.out.println("RESOLVIENDO ROLES");

		Set<String> roles = requestedRoles == null || requestedRoles.isEmpty()
				? Set.of(RoleName.CLIENT.name())
				: requestedRoles;

		return roles.stream()
				.map(role -> RoleName.valueOf(role.toUpperCase()))
				.map(this::findOrCreateRole)
				.collect(Collectors.toCollection(HashSet::new));
	}

	private Role findOrCreateRole(RoleName roleName) {

		System.out.println("BUSCANDO/CREANDO ROLE: " + roleName);

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