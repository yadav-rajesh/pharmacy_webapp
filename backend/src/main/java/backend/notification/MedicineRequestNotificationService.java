package backend.notification;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import backend.api.MedicineRequestIntegrationPreviewResponse;
import backend.medicinerequest.MedicineRequestStatus;
import backend.persistence.entity.MedicineRequest;
import backend.persistence.entity.PrescriptionFile;

@Service
public class MedicineRequestNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(
		MedicineRequestNotificationService.class
	);

	private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	@Nullable
	private final JavaMailSender mailSender;
	private final MedicineRequestNotificationProperties properties;

	public MedicineRequestNotificationService(
		@Nullable JavaMailSender mailSender,
		MedicineRequestNotificationProperties properties
	) {
		this.mailSender = mailSender;
		this.properties = properties;
	}

	public void sendNewRequestAlert(MedicineRequest medicineRequest) {
		if (!properties.getEmail().isEnabled()) {
			return;
		}

		var recipient = clean(properties.getEmail().getRecipient());

		if (recipient == null) {
			logger.warn("Email alerts are enabled but no recipient is configured.");
			return;
		}

		if (mailSender == null) {
			logger.warn("Email alerts are enabled but no JavaMailSender is available.");
			return;
		}

		try {
			var message = new SimpleMailMessage();
			var from = clean(properties.getEmail().getFrom());

			if (from != null) {
				message.setFrom(from);
			}

			message.setTo(recipient);
			message.setSubject(buildEmailSubject(medicineRequest));
			message.setText(buildEmailBody(medicineRequest));
			mailSender.send(message);
		} catch (MailException exception) {
			logger.warn(
				"Could not send medicine request alert for request {}.",
				medicineRequest.getRequestId(),
				exception
			);
		}
	}

	public MedicineRequestIntegrationPreviewResponse buildIntegrationPreview(
		MedicineRequest medicineRequest
	) {
		var whatsAppMessage = buildWhatsAppMessage(medicineRequest);
		var recipientNumber = normalizePhoneDigits(
			properties.getWhatsApp().getRecipientNumber()
		);
		String whatsAppShareUrl = null;

		if (recipientNumber != null) {
			whatsAppShareUrl = "https://wa.me/" + recipientNumber + "?text=" + URLEncoder.encode(
				whatsAppMessage,
				StandardCharsets.UTF_8
			);
		}

		return new MedicineRequestIntegrationPreviewResponse(
			medicineRequest.getRequestId(),
			properties.getEmail().isEnabled(),
			clean(properties.getEmail().getRecipient()),
			recipientNumber,
			whatsAppMessage,
			whatsAppShareUrl,
			Arrays.stream(MedicineRequestStatus.values()).map(Enum::name).toList()
		);
	}

	private String buildEmailSubject(MedicineRequest medicineRequest) {
		var prefix = clean(properties.getEmail().getSubjectPrefix());
		var subject = "New medicine request - " + medicineRequest.getName();
		return prefix == null ? subject : prefix + " " + subject;
	}

	private String buildEmailBody(MedicineRequest medicineRequest) {
		return "A new medicine request was received.\n\n"
			+ "Request ID: " + medicineRequest.getRequestId() + "\n"
			+ "Customer name: " + medicineRequest.getName() + "\n"
			+ "Phone: " + medicineRequest.getPhone() + "\n"
			+ "Medicine name: " + medicineRequest.getMedicineName() + "\n"
			+ "Address: " + medicineRequest.getAddress() + "\n"
			+ "Current status: " + normalizeStatus(medicineRequest) + "\n"
			+ "Created at: " + TIMESTAMP_FORMATTER.format(
				medicineRequest.getCreatedAt().atOffset(ZoneOffset.UTC)
			) + "\n"
			+ "Prescription: " + formatPrescription(medicineRequest.getPrescriptionFile()) + "\n";
	}

	private String buildWhatsAppMessage(MedicineRequest medicineRequest) {
		return "New medicine request\n"
			+ "Request ID: " + medicineRequest.getRequestId() + "\n"
			+ "Customer: " + medicineRequest.getName() + "\n"
			+ "Phone: " + medicineRequest.getPhone() + "\n"
			+ "Medicine: " + medicineRequest.getMedicineName() + "\n"
			+ "Address: " + medicineRequest.getAddress() + "\n"
			+ "Current status: " + normalizeStatus(medicineRequest) + "\n"
			+ "Prescription: " + formatPrescription(medicineRequest.getPrescriptionFile()) + "\n"
			+ "Created at: " + TIMESTAMP_FORMATTER.format(
				medicineRequest.getCreatedAt().atOffset(ZoneOffset.UTC)
			) + "\n";
	}

	private String formatPrescription(PrescriptionFile prescriptionFile) {
		if (prescriptionFile == null) {
			return "Not uploaded";
		}

		return prescriptionFile.getOriginalFilename() + " (" + prescriptionFile.getSavedPath() + ")";
	}

	private String normalizeStatus(MedicineRequest medicineRequest) {
		return MedicineRequestStatus.fromStoredValue(medicineRequest.getStatus()).name();
	}

	private String clean(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}

		return value.trim();
	}

	private String normalizePhoneDigits(String value) {
		var cleanedValue = clean(value);

		if (cleanedValue == null) {
			return null;
		}

		var digits = cleanedValue.replaceAll("\\D", "");
		return digits.isBlank() ? null : digits;
	}
}
