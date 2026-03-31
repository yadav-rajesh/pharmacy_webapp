package backend.notification;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import backend.persistence.entity.MedicineRequest;
import backend.persistence.entity.PrescriptionFile;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

class MedicineRequestNotificationServiceTest {

	@Test
	void sendsEmailAlertAndBuildsDashboardPreview() {
		var mailSender = new CapturingMailSender();
		var properties = new MedicineRequestNotificationProperties();
		properties.getEmail().setEnabled(true);
		properties.getEmail().setRecipient("alerts@padmavatimedicals.test");
		properties.getEmail().setFrom("no-reply@padmavatimedicals.test");
		properties.getEmail().setSubjectPrefix("[Padmavati Medicals]");
		properties.getWhatsApp().setRecipientNumber("+91 97300 86267");

		var service = new MedicineRequestNotificationService(mailSender, properties);
		var medicineRequest = createMedicineRequest();

		service.sendNewRequestAlert(medicineRequest);
		var preview = service.buildIntegrationPreview(medicineRequest);

		assertThat(mailSender.getLastMessage()).isNotNull();
		assertThat(mailSender.getLastMessage().getTo()).containsExactly("alerts@padmavatimedicals.test");
		assertThat(mailSender.getLastMessage().getFrom()).isEqualTo("no-reply@padmavatimedicals.test");
		assertThat(mailSender.getLastMessage().getSubject()).isEqualTo(
			"[Padmavati Medicals] New medicine request - Rajesh Yadav"
		);
		assertThat(mailSender.getLastMessage().getText()).contains(
			"Request ID: request-123",
			"Medicine name: Paracetamol 650",
			"Prescription: rx.pdf (request-123/rx.pdf)"
		);

		assertThat(preview.requestId()).isEqualTo("request-123");
		assertThat(preview.emailAlertsEnabled()).isTrue();
		assertThat(preview.emailRecipient()).isEqualTo("alerts@padmavatimedicals.test");
		assertThat(preview.whatsAppRecipientNumber()).isEqualTo("919730086267");
		assertThat(preview.whatsAppMessage()).contains(
			"Request ID: request-123",
			"Current status: NEW"
		);
		assertThat(preview.whatsAppShareUrl()).startsWith("https://wa.me/919730086267?text=");
		assertThat(preview.availableStatuses()).containsExactly("NEW", "CONTACTED", "FULFILLED");
	}

	@Test
	void skipsEmailWhenAlertsAreDisabled() {
		var mailSender = new CapturingMailSender();
		var service = new MedicineRequestNotificationService(
			mailSender,
			new MedicineRequestNotificationProperties()
		);

		service.sendNewRequestAlert(createMedicineRequest());

		assertThat(mailSender.getLastMessage()).isNull();
	}

	private MedicineRequest createMedicineRequest() {
		var medicineRequest = new MedicineRequest();
		medicineRequest.setRequestId("request-123");
		medicineRequest.setName("Rajesh Yadav");
		medicineRequest.setPhone("9730086267");
		medicineRequest.setMedicineName("Paracetamol 650");
		medicineRequest.setAddress("Ichalkaranji");
		medicineRequest.setStatus("NEW");
		medicineRequest.setCreatedAt(Instant.parse("2026-03-31T10:15:30Z"));

		var prescriptionFile = new PrescriptionFile();
		prescriptionFile.setRequestId("request-123");
		prescriptionFile.setOriginalFilename("rx.pdf");
		prescriptionFile.setSavedPath("request-123/rx.pdf");
		prescriptionFile.setUploadedAt(Instant.parse("2026-03-31T10:16:00Z"));
		medicineRequest.setPrescriptionFile(prescriptionFile);

		return medicineRequest;
	}

	private static final class CapturingMailSender implements JavaMailSender {

		private SimpleMailMessage lastMessage;

		private SimpleMailMessage getLastMessage() {
			return lastMessage;
		}

		@Override
		public MimeMessage createMimeMessage() {
			return new MimeMessage(Session.getInstance(new Properties()));
		}

		@Override
		public MimeMessage createMimeMessage(InputStream contentStream) {
			try {
				return new MimeMessage(Session.getInstance(new Properties()), contentStream);
			} catch (Exception exception) {
				throw new IllegalStateException(exception);
			}
		}

		@Override
		public void send(MimeMessage mimeMessage) {
			throw new UnsupportedOperationException("MimeMessage sending is not used in this test.");
		}

		@Override
		public void send(MimeMessage... mimeMessages) {
			throw new UnsupportedOperationException("MimeMessage sending is not used in this test.");
		}

		@Override
		public void send(SimpleMailMessage simpleMessage) {
			lastMessage = new SimpleMailMessage(simpleMessage);
		}

		@Override
		public void send(SimpleMailMessage... simpleMessages) {
			if (simpleMessages.length > 0) {
				lastMessage = new SimpleMailMessage(simpleMessages[simpleMessages.length - 1]);
			}
		}
	}
}
