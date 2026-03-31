package backend.medicinerequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateMedicineRequest(
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

	@NotBlank(message = "Medicine name is required.")
	@Size(max = 255, message = "Medicine name must be 255 characters or fewer.")
	String medicineName,

	@NotBlank(message = "Address is required.")
	@Size(max = 500, message = "Address must be 500 characters or fewer.")
	String address
) {
}
