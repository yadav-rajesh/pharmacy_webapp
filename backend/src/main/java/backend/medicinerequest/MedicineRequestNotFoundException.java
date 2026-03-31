package backend.medicinerequest;

public class MedicineRequestNotFoundException extends RuntimeException {

	public MedicineRequestNotFoundException(String requestId) {
		super("Medicine request not found: " + requestId);
	}
}
