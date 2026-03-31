package backend.medicinerequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import backend.api.MedicineRequestDetailResponse;
import backend.api.MedicineRequestResponse;
import backend.api.MedicineRequestSummaryResponse;
import backend.api.PrescriptionFileResponse;
import backend.persistence.entity.MedicineRequest;
import backend.persistence.entity.PrescriptionFile;
import backend.persistence.repository.MedicineRequestRepository;

@Service
public class MedicineRequestService {

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf");

	private final Path storageRoot;
	private final MedicineRequestRepository medicineRequestRepository;

	public MedicineRequestService(
		@Value("${medicine-requests.storage-dir}") String storageDirectory,
		MedicineRequestRepository medicineRequestRepository
	) {
		this.storageRoot = Path.of(storageDirectory).toAbsolutePath().normalize();
		this.medicineRequestRepository = medicineRequestRepository;
	}

	@Transactional
	public MedicineRequestResponse create(
		CreateMedicineRequest request,
		MultipartFile prescription
	) {
		var requestId = UUID.randomUUID().toString();
		var createdAt = Instant.now();
		var cleanedRequest = clean(request);
		StoredPrescriptionUpload prescriptionUpload = null;

		try {
			prescriptionUpload = storePrescription(requestId, prescription);

			var medicineRequest = new MedicineRequest();
			medicineRequest.setRequestId(requestId);
			medicineRequest.setName(cleanedRequest.name());
			medicineRequest.setPhone(cleanedRequest.phone());
			medicineRequest.setMedicineName(cleanedRequest.medicineName());
			medicineRequest.setAddress(cleanedRequest.address());
			medicineRequest.setStatus(MedicineRequestStatus.NEW.name());
			medicineRequest.setCreatedAt(createdAt);

			if (prescriptionUpload != null) {
				var prescriptionFile = new PrescriptionFile();
				prescriptionFile.setRequestId(requestId);
				prescriptionFile.setOriginalFilename(prescriptionUpload.originalFilename());
				prescriptionFile.setSavedPath(prescriptionUpload.savedPath());
				prescriptionFile.setUploadedAt(prescriptionUpload.uploadedAt());
				medicineRequest.setPrescriptionFile(prescriptionFile);
			}

			medicineRequestRepository.save(medicineRequest);
		} catch (IOException exception) {
			cleanupStoredPrescription(prescriptionUpload);
			throw new MedicineRequestStorageException(
				"Could not store medicine request.",
				exception
			);
		} catch (RuntimeException exception) {
			cleanupStoredPrescription(prescriptionUpload);
			throw exception;
		}

		return new MedicineRequestResponse(
			requestId,
			MedicineRequestStatus.NEW.name(),
			"Medicine request received successfully.",
			prescriptionUpload != null,
			createdAt
		);
	}

	@Transactional(readOnly = true)
	public List<MedicineRequestSummaryResponse> list() {
		return medicineRequestRepository.findAllByOrderByCreatedAtDescIdDesc()
			.stream()
			.map(this::toSummaryResponse)
			.toList();
	}

	@Transactional(readOnly = true)
	public MedicineRequestDetailResponse get(String requestId) {
		return toDetailResponse(findRequest(requestId));
	}

	@Transactional
	public MedicineRequestDetailResponse updateStatus(String requestId, String status) {
		var medicineRequest = findRequest(requestId);
		var nextStatus = MedicineRequestStatus.fromInput(status);
		medicineRequest.setStatus(nextStatus.name());
		return toDetailResponse(medicineRequestRepository.save(medicineRequest));
	}

	private CreateMedicineRequest clean(CreateMedicineRequest request) {
		return new CreateMedicineRequest(
			requireText("Name", request.name()),
			requireText("Phone number", request.phone()),
			requireText("Medicine name", request.medicineName()),
			requireText("Address", request.address())
		);
	}

	private String requireText(String label, String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(label + " is required.");
		}

		return value.trim();
	}

	private MedicineRequest findRequest(String requestId) {
		var cleanedRequestId = requireText("Request ID", requestId);
		return medicineRequestRepository.findByRequestId(cleanedRequestId)
			.orElseThrow(() -> new MedicineRequestNotFoundException(cleanedRequestId));
	}

	private MedicineRequestSummaryResponse toSummaryResponse(MedicineRequest medicineRequest) {
		return new MedicineRequestSummaryResponse(
			medicineRequest.getRequestId(),
			medicineRequest.getName(),
			medicineRequest.getPhone(),
			medicineRequest.getMedicineName(),
			normalizeStatus(medicineRequest.getStatus()),
			medicineRequest.getPrescriptionFile() != null,
			medicineRequest.getCreatedAt()
		);
	}

	private MedicineRequestDetailResponse toDetailResponse(MedicineRequest medicineRequest) {
		return new MedicineRequestDetailResponse(
			medicineRequest.getRequestId(),
			medicineRequest.getName(),
			medicineRequest.getPhone(),
			medicineRequest.getMedicineName(),
			medicineRequest.getAddress(),
			normalizeStatus(medicineRequest.getStatus()),
			medicineRequest.getCreatedAt(),
			toPrescriptionFileResponse(medicineRequest.getPrescriptionFile())
		);
	}

	private PrescriptionFileResponse toPrescriptionFileResponse(PrescriptionFile prescriptionFile) {
		if (prescriptionFile == null) {
			return null;
		}

		return new PrescriptionFileResponse(
			prescriptionFile.getOriginalFilename(),
			prescriptionFile.getSavedPath(),
			prescriptionFile.getUploadedAt()
		);
	}

	private String normalizeStatus(String storedStatus) {
		return MedicineRequestStatus.fromStoredValue(storedStatus).name();
	}

	private StoredPrescriptionUpload storePrescription(String requestId, MultipartFile prescription)
		throws IOException {
		if (prescription == null || prescription.isEmpty()) {
			return null;
		}

		Files.createDirectories(storageRoot);

		var requestDirectory = storageRoot.resolve(requestId).normalize();
		Files.createDirectories(requestDirectory);

		var originalFilename = StringUtils.cleanPath(
			prescription.getOriginalFilename() == null ? "" : prescription.getOriginalFilename()
		);

		if (originalFilename.isBlank()) {
			throw new IllegalArgumentException("Prescription file name is invalid.");
		}

		if (originalFilename.contains("..")) {
			throw new IllegalArgumentException("Prescription file name is invalid.");
		}

		var extension = StringUtils.getFilenameExtension(originalFilename);

		if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase(Locale.ROOT))) {
			throw new IllegalArgumentException(
				"Prescription file must be a PDF, JPG, JPEG, or PNG."
			);
		}

		var storedFile = requestDirectory.resolve(originalFilename).normalize();

		if (!storedFile.startsWith(requestDirectory)) {
			throw new IllegalArgumentException("Prescription file name is invalid.");
		}

		try (InputStream inputStream = prescription.getInputStream()) {
			Files.copy(inputStream, storedFile, StandardCopyOption.REPLACE_EXISTING);
		}

		var uploadedAt = Instant.now();
		var savedPath = storageRoot.relativize(storedFile).toString().replace('\\', '/');

		return new StoredPrescriptionUpload(
			requestId,
			originalFilename,
			savedPath,
			uploadedAt
		);
	}

	private void cleanupStoredPrescription(StoredPrescriptionUpload prescriptionUpload) {
		if (prescriptionUpload == null) {
			return;
		}

		try {
			var storedFile = storageRoot.resolve(prescriptionUpload.savedPath()).normalize();
			Files.deleteIfExists(storedFile);
			var requestDirectory = storedFile.getParent();

			if (requestDirectory != null && Files.isDirectory(requestDirectory)) {
				try (var files = Files.list(requestDirectory)) {
					if (files.findAny().isEmpty()) {
						Files.deleteIfExists(requestDirectory);
					}
				}
			}
		} catch (IOException ignored) {
			// Best-effort cleanup for files stored before a database failure.
		}
	}
}
