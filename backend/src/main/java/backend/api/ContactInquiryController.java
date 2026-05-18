package backend.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.contactinquiry.ContactInquiryService;
import backend.contactinquiry.CreateContactInquiry;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact-inquiries")
public class ContactInquiryController {

	private final ContactInquiryService contactInquiryService;

	public ContactInquiryController(ContactInquiryService contactInquiryService) {
		this.contactInquiryService = contactInquiryService;
	}

	@PostMapping
	public ResponseEntity<ContactInquiryResponse> createContactInquiry(
		@Valid @RequestBody CreateContactInquiry request
	) {
		var response = contactInquiryService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
