package backend.api;

import java.time.Instant;

public record PrescriptionFileResponse(
	String originalFilename,
	String savedPath,
	Instant uploadedAt
) {
}
