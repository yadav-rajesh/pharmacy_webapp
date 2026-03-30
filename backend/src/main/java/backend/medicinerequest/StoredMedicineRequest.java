package backend.medicinerequest;

import java.time.Instant;

public record StoredMedicineRequest(
	String requestId,
	String name,
	String phone,
	String medicineName,
	String address,
	String prescriptionFileName,
	String status,
	Instant createdAt
) {
}
