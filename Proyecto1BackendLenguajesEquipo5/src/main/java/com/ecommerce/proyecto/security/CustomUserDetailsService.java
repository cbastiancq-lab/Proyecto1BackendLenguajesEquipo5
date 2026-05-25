package com.ecommerce.proyecto.security;

import com.ecommerce.proyecto.domain.model.User;
import com.ecommerce.proyecto.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String[] authorities = user.getRoles().stream()
				.map(role -> "ROLE_" + role.getName().name())
				.toArray(String[]::new);

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(authorities)
				.disabled(!user.isEnabled())
				.build();
	}
}
