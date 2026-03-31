package backend.api;

import java.time.Instant;

public record MedicineRequestSummaryResponse(
	String requestId,
	String name,
	String phone,
	String medicineName,
	String status,
	boolean prescriptionUploaded,
	Instant createdAt
) {
}
