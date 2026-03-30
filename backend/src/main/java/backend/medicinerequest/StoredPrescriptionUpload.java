package backend.medicinerequest;

import java.time.Instant;

public record StoredPrescriptionUpload(
	String requestId,
	String originalFilename,
	String savedPath,
	Instant uploadedAt
) {
}
