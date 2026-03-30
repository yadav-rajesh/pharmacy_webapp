package backend.medicinerequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import backend.api.MedicineRequestResponse;

@Service
public class MedicineRequestService {

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf");

	private final Path storageRoot;

	public MedicineRequestService(@Value("${medicine-requests.storage-dir}") String storageDirectory) {
		this.storageRoot = Path.of(storageDirectory).toAbsolutePath().normalize();
	}

	public MedicineRequestResponse create(
		CreateMedicineRequest request,
		MultipartFile prescription
	) {
		var requestId = UUID.randomUUID().toString();
		var createdAt = Instant.now();
		var cleanedRequest = clean(request);
		var prescriptionUpload = storeRequestFiles(requestId, cleanedRequest, prescription, createdAt);

		return new MedicineRequestResponse(
			requestId,
			"RECEIVED",
			"Medicine request received successfully.",
			prescriptionUpload != null,
			createdAt
		);
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

	private StoredPrescriptionUpload storeRequestFiles(
		String requestId,
		CreateMedicineRequest request,
		MultipartFile prescription,
		Instant createdAt
	) {
		try {
			Files.createDirectories(storageRoot);

			var requestDirectory = storageRoot.resolve(requestId).normalize();
			Files.createDirectories(requestDirectory);

			var prescriptionUpload = storePrescription(requestDirectory, requestId, prescription);
			var storedRequest = new StoredMedicineRequest(
				requestId,
				request.name(),
				request.phone(),
				request.medicineName(),
				request.address(),
				"RECEIVED",
				createdAt,
				prescriptionUpload == null ? null : prescriptionUpload.originalFilename(),
				prescriptionUpload == null ? null : prescriptionUpload.savedPath(),
				prescriptionUpload == null ? null : prescriptionUpload.uploadedAt()
			);

			Files.writeString(requestDirectory.resolve("request.txt"), buildMetadata(storedRequest));

			return prescriptionUpload;
		} catch (IOException exception) {
			throw new MedicineRequestStorageException(
				"Could not store medicine request.",
				exception
			);
		}
	}

	private StoredPrescriptionUpload storePrescription(
		Path requestDirectory,
		String requestId,
		MultipartFile prescription
	) throws IOException {
		if (prescription == null || prescription.isEmpty()) {
			return null;
		}

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

	private String buildMetadata(StoredMedicineRequest storedRequest) {
		return """
			requestId=%s
			status=%s
			createdAt=%s
			name=%s
			phone=%s
			medicineName=%s
			address=%s
			prescriptionOriginalFilename=%s
			prescriptionSavedPath=%s
			prescriptionUploadedAt=%s
			""".formatted(
			storedRequest.requestId(),
			storedRequest.status(),
			storedRequest.createdAt(),
			storedRequest.name(),
			storedRequest.phone(),
			storedRequest.medicineName(),
			storedRequest.address(),
			storedRequest.prescriptionOriginalFilename() == null ? "" : storedRequest.prescriptionOriginalFilename(),
			storedRequest.prescriptionSavedPath() == null ? "" : storedRequest.prescriptionSavedPath(),
			storedRequest.prescriptionUploadedAt() == null ? "" : storedRequest.prescriptionUploadedAt()
		);
	}
}
