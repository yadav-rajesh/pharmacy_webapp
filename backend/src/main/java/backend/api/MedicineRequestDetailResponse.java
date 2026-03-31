package backend.api;

import java.time.Instant;

public record MedicineRequestDetailResponse(
	String requestId,
	String name,
	String phone,
	String medicineName,
	String address,
	String status,
	Instant createdAt,
	PrescriptionFileResponse prescriptionFile
) {
}
