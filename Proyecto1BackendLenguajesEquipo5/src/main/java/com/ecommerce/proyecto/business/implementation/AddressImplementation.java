package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.AddressService;
import com.ecommerce.proyecto.data.entity.Address;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.AddressRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.dto.AddressDto;
import com.ecommerce.proyecto.domain.request.AddressRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressImplementation implements AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	public AddressImplementation(AddressRepository addressRepository, UserRepository userRepository) {
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AddressDto> findAll() {
		return addressRepository.findAll().stream().map(this::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AddressDto> findByUserId(Long userId) {
		return addressRepository.findByUserId(userId).stream().map(this::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AddressDto> findById(Long id) {
		return addressRepository.findById(id).map(this::toDto);
	}

	@Override
	@Transactional
	public AddressDto create(AddressRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Address address = new Address();
		address.setUser(user);
		address.setProvince(request.province());
		address.setCity(request.city());
		address.setDistrict(request.district());
		address.setDetails(request.details());
		address.setMainAddress(Boolean.TRUE.equals(request.mainAddress()));
		return toDto(addressRepository.save(address));
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		if (!addressRepository.existsById(id)) {
			return false;
		}
		addressRepository.deleteById(id);
		return true;
	}

	private AddressDto toDto(Address address) {
		return new AddressDto(
				address.getId(),
				address.getUser().getId(),
				address.getProvince(),
				address.getCity(),
				address.getDistrict(),
				address.getDetails(),
				address.isMainAddress()
		);
	}
}
