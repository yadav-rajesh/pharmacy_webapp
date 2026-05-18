package backend.contactinquiry;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.api.ContactInquiryResponse;
import backend.persistence.entity.ContactInquiry;
import backend.persistence.repository.ContactInquiryRepository;

@Service
public class ContactInquiryService {

	private final ContactInquiryRepository contactInquiryRepository;

	public ContactInquiryService(ContactInquiryRepository contactInquiryRepository) {
		this.contactInquiryRepository = contactInquiryRepository;
	}

	@Transactional
	public ContactInquiryResponse create(CreateContactInquiry request) {
		var createdAt = Instant.now();
		var inquiryId = UUID.randomUUID().toString();

		var contactInquiry = new ContactInquiry();
		contactInquiry.setInquiryId(inquiryId);
		contactInquiry.setName(request.name().trim());
		contactInquiry.setPhone(request.phone().trim());
		contactInquiry.setMessage(request.message().trim());
		contactInquiry.setStatus("NEW");
		contactInquiry.setCreatedAt(createdAt);

		contactInquiryRepository.save(contactInquiry);

		return new ContactInquiryResponse(
			inquiryId,
			"NEW",
			"Contact inquiry received successfully.",
			createdAt
		);
	}
}
