package backend.medicinerequest;

import java.util.Locale;

public enum MedicineRequestStatus {
	NEW,
	CONTACTED,
	FULFILLED;

	public static MedicineRequestStatus fromInput(String value) {
		if (value == null || value.isBlank()) {
			throw invalidStatus();
		}

		return fromNormalizedValue(value.trim().toUpperCase(Locale.ROOT));
	}

	public static MedicineRequestStatus fromStoredValue(String value) {
		if (value == null || value.isBlank()) {
			return NEW;
		}

		return fromNormalizedValue(value.trim().toUpperCase(Locale.ROOT));
	}

	private static MedicineRequestStatus fromNormalizedValue(String value) {
		return switch (value) {
			case "NEW", "RECEIVED" -> NEW;
			case "CONTACTED" -> CONTACTED;
			case "FULFILLED" -> FULFILLED;
			default -> throw invalidStatus();
		};
	}

	private static IllegalArgumentException invalidStatus() {
		return new IllegalArgumentException(
			"Status must be one of: NEW, CONTACTED, FULFILLED."
		);
	}
}
