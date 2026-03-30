package backend.api;

import java.time.Instant;

public record MedicineRequestResponse(
	String requestId,
	String status,
	String message,
	boolean prescriptionUploaded,
	Instant createdAt
) {
}
