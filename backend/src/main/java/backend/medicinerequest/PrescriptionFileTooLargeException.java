package backend.medicinerequest;

public class PrescriptionFileTooLargeException extends RuntimeException {

	public PrescriptionFileTooLargeException(String message) {
		super(message);
	}
}
