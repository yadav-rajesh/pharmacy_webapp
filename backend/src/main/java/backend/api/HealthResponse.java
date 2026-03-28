package backend.api;

import java.time.Instant;

public record HealthResponse(
	String status,
	String service,
	String message,
	Instant timestamp
) {
}
