package backend.api;

import jakarta.validation.constraints.NotBlank;

public record UpdateMedicineRequestStatusRequest(
	@NotBlank(message = "Status is required.")
	String status
) {
}
