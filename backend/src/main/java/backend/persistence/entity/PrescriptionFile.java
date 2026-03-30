package backend.persistence.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescription_files")
public class PrescriptionFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "medicine_request_id", nullable = false, unique = true)
	private MedicineRequest medicineRequest;

	@Column(nullable = false, length = 36)
	private String requestId;

	@Column(nullable = false, length = 255)
	private String originalFilename;

	@Column(nullable = false, length = 500)
	private String savedPath;

	@Column(nullable = false)
	private Instant uploadedAt;

	public Long getId() {
		return id;
	}

	public MedicineRequest getMedicineRequest() {
		return medicineRequest;
	}

	public void setMedicineRequest(MedicineRequest medicineRequest) {
		this.medicineRequest = medicineRequest;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getSavedPath() {
		return savedPath;
	}

	public void setSavedPath(String savedPath) {
		this.savedPath = savedPath;
	}

	public Instant getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Instant uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
}
