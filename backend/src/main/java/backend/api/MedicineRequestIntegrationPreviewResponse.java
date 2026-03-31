package backend.api;

import java.util.List;

public record MedicineRequestIntegrationPreviewResponse(
	String requestId,
	boolean emailAlertsEnabled,
	String emailRecipient,
	String whatsAppRecipientNumber,
	String whatsAppMessage,
	String whatsAppShareUrl,
	List<String> availableStatuses
) {
}
