package backend.contactinquiry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateContactInquiry(
	@NotBlank(message = "Name is required.")
	@Size(max = 120, message = "Name must be 120 characters or fewer.")
	String name,

	@NotBlank(message = "Phone number is required.")
	@Size(max = 20, message = "Phone number must be 20 characters or fewer.")
	@Pattern(
		regexp = "^[0-9+()\\-\\s]{7,20}$",
		message = "Phone number must contain 7 to 20 valid characters."
	)
	String phone,

	@NotBlank(message = "Message is required.")
	@Size(max = 1000, message = "Message must be 1000 characters or fewer.")
	String message
) {
}
