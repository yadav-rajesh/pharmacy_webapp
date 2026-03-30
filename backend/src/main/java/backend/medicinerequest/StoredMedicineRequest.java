package backend.medicinerequest;

import java.time.Instant;

public record StoredMedicineRequest(
	String requestId,
	String name,
	String phone,
	String medicineName,
	String address,
	String status,
	Instant createdAt,
	String prescriptionOriginalFilename,
	String prescriptionSavedPath,
	Instant prescriptionUploadedAt
) {
}
