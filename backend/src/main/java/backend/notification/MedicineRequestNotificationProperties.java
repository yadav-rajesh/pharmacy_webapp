package backend.notification;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "medicine-requests.notifications")
public class MedicineRequestNotificationProperties {

	private final Email email = new Email();
	private final WhatsApp whatsApp = new WhatsApp();

	public Email getEmail() {
		return email;
	}

	public WhatsApp getWhatsApp() {
		return whatsApp;
	}

	public static class Email {

		private boolean enabled;
		private String recipient = "";
		private String from = "";
		private String subjectPrefix = "[Padmavati Medicals]";

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getRecipient() {
			return recipient;
		}

		public void setRecipient(String recipient) {
			this.recipient = recipient;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getSubjectPrefix() {
			return subjectPrefix;
		}

		public void setSubjectPrefix(String subjectPrefix) {
			this.subjectPrefix = subjectPrefix;
		}
	}

	public static class WhatsApp {

		private String recipientNumber = "";

		public String getRecipientNumber() {
			return recipientNumber;
		}

		public void setRecipientNumber(String recipientNumber) {
			this.recipientNumber = recipientNumber;
		}
	}
}
