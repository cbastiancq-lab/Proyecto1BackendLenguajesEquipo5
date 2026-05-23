package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.ContactInformationService;
import com.ecommerce.proyecto.data.entity.ContactInformation;
import com.ecommerce.proyecto.data.entity.User;
import com.ecommerce.proyecto.data.repository.ContactInformationRepository;
import com.ecommerce.proyecto.data.repository.UserRepository;
import com.ecommerce.proyecto.domain.dto.ContactInformationDto;
import com.ecommerce.proyecto.domain.request.ContactInformationRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactInformationImplementation implements ContactInformationService {

	private final ContactInformationRepository contactRepository;
	private final UserRepository userRepository;

	public ContactInformationImplementation(ContactInformationRepository contactRepository, UserRepository userRepository) {
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContactInformationDto> findAll() {
		return contactRepository.findAll().stream().map(this::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ContactInformationDto> findById(Long id) {
		return contactRepository.findById(id).map(this::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ContactInformationDto> findByUserId(Long userId) {
		return contactRepository.findByUserId(userId).map(this::toDto);
	}

	@Override
	@Transactional
	public ContactInformationDto create(ContactInformationRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		ContactInformation contact = new ContactInformation();
		contact.setUser(user);
		contact.setPhone(request.phone());
		contact.setSecondaryPhone(request.secondaryPhone());
		return toDto(contactRepository.save(contact));
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		if (!contactRepository.existsById(id)) {
			return false;
		}
		contactRepository.deleteById(id);
		return true;
	}

	private ContactInformationDto toDto(ContactInformation contact) {
		return new ContactInformationDto(
				contact.getId(),
				contact.getUser().getId(),
				contact.getPhone(),
				contact.getSecondaryPhone()
		);
	}
}
