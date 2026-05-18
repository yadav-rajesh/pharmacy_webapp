package backend.api;

import java.time.Instant;

public record ContactInquiryResponse(
	String inquiryId,
	String status,
	String message,
	Instant createdAt
) {
}
