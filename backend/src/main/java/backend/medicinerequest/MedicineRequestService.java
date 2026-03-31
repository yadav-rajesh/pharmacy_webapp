package backend.medicinerequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import backend.api.MedicineRequestDetailResponse;
import backend.api.MedicineRequestIntegrationPreviewResponse;
import backend.api.MedicineRequestResponse;
import backend.api.MedicineRequestSummaryResponse;
import backend.api.PrescriptionFileResponse;
import backend.notification.MedicineRequestNotificationService;
import backend.persistence.entity.MedicineRequest;
import backend.persistence.entity.PrescriptionFile;
import backend.persistence.repository.MedicineRequestRepository;

@Service
public class MedicineRequestService {

	private final Path storageRoot;
	private final Set<String> allowedExtensions;
	private final Set<String> allowedContentTypes;
	private final long maxFileSizeBytes;
	private final MedicineRequestRepository medicineRequestRepository;
	private final MedicineRequestNotificationService medicineRequestNotificationService;

	public MedicineRequestService(
		@Value("${medicine-requests.storage-dir:data/medicine-requests}") String storageDirectory,
		@Value("${medicine-requests.upload.max-file-size-bytes:5242880}") long maxFileSizeBytes,
		@Value("${medicine-requests.upload.allowed-extensions:jpg,jpeg,png,pdf}") String allowedExtensions,
		@Value("${medicine-requests.upload.allowed-content-types:image/jpeg,image/png,application/pdf}") String allowedContentTypes,
		MedicineRequestRepository medicineRequestRepository,
		MedicineRequestNotificationService medicineRequestNotificationService
	) {
		this.storageRoot = Path.of(storageDirectory).toAbsolutePath().normalize();
		this.allowedExtensions = splitAndNormalize(allowedExtensions);
		this.allowedContentTypes = splitAndNormalize(allowedContentTypes);
		this.maxFileSizeBytes = maxFileSizeBytes;
		this.medicineRequestRepository = medicineRequestRepository;
		this.medicineRequestNotificationService = medicineRequestNotificationService;
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

			var savedRequest = medicineRequestRepository.save(medicineRequest);
			medicineRequestNotificationService.sendNewRequestAlert(savedRequest);
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

	@Transactional(readOnly = true)
	public MedicineRequestIntegrationPreviewResponse getIntegrationPreview(String requestId) {
		return medicineRequestNotificationService.buildIntegrationPreview(findRequest(requestId));
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

		if (prescription.getSize() > maxFileSizeBytes) {
			throw new PrescriptionFileTooLargeException(buildFileSizeMessage());
		}

		Files.createDirectories(storageRoot);

		var requestDirectory = storageRoot.resolve(requestId).normalize();
		Files.createDirectories(requestDirectory);

		var originalFilename = StringUtils.cleanPath(
			prescription.getOriginalFilename() == null ? "" : prescription.getOriginalFilename()
		);

		if (originalFilename.isBlank() || originalFilename.contains("..")) {
			throw new IllegalArgumentException("Prescription file name is invalid.");
		}

		var extension = StringUtils.getFilenameExtension(originalFilename);

		if (extension == null || !allowedExtensions.contains(extension.toLowerCase(Locale.ROOT))) {
			throw new IllegalArgumentException(
				"Prescription file must be a PDF, JPG, JPEG, or PNG."
			);
		}

		var contentType = prescription.getContentType();

		if (contentType != null && !contentType.isBlank()) {
			var normalizedContentType = contentType.toLowerCase(Locale.ROOT);

			if (!allowedContentTypes.contains(normalizedContentType)) {
				throw new IllegalArgumentException(
					"Prescription file type must be a PDF, JPG, JPEG, or PNG."
				);
			}
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

	private String buildFileSizeMessage() {
		if (maxFileSizeBytes < 1024 * 1024) {
			var maxKilobytes = maxFileSizeBytes / 1024d;
			return String.format(Locale.ROOT, "Prescription file must be %.0f KB or smaller.", maxKilobytes);
		}

		var maxMegabytes = maxFileSizeBytes / 1024d / 1024d;
		return String.format(Locale.ROOT, "Prescription file must be %.1f MB or smaller.", maxMegabytes);
	}

	private Set<String> splitAndNormalize(String rawValue) {
		return Arrays.stream(rawValue.split(","))
			.map(String::trim)
			.filter(value -> !value.isBlank())
			.map(value -> value.toLowerCase(Locale.ROOT))
			.collect(Collectors.toUnmodifiableSet());
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
