package backend.medicinerequest;

public record CreateMedicineRequest(
	String name,
	String phone,
	String medicineName,
	String address
) {
}
