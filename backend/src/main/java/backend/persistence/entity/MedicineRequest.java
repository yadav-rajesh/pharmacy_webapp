package backend.persistence.entity;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicine_requests")
public class MedicineRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 36)
	private String requestId;

	@Column(nullable = false, length = 120)
	private String name;

	@Column(nullable = false, length = 40)
	private String phone;

	@Column(nullable = false, length = 255)
	private String medicineName;

	@Column(nullable = false, length = 500)
	private String address;

	@Column(nullable = false, length = 40)
	private String status;

	@Column(nullable = false)
	private Instant createdAt;

	@OneToOne(mappedBy = "medicineRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private PrescriptionFile prescriptionFile;

	public Long getId() {
		return id;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public PrescriptionFile getPrescriptionFile() {
		return prescriptionFile;
	}

	public void setPrescriptionFile(PrescriptionFile prescriptionFile) {
		this.prescriptionFile = prescriptionFile;

		if (prescriptionFile != null) {
			prescriptionFile.setMedicineRequest(this);
		}
	}
}
