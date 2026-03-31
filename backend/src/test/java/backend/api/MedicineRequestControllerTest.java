package backend.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import backend.medicinerequest.MedicineRequestStatus;
import backend.persistence.repository.MedicineRequestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
	"medicine-requests.storage-dir=target/test-requests",
	"spring.datasource.url=jdbc:h2:mem:medicine-request-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
class MedicineRequestControllerTest {

	private static final Path STORAGE_ROOT = Path.of("target/test-requests");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MedicineRequestRepository medicineRequestRepository;

	@BeforeEach
	@AfterEach
	void clearState() throws IOException {
		medicineRequestRepository.deleteAll();
		deleteDirectory(STORAGE_ROOT);
	}

	@Test
	void storesMedicineRequestAndReturnsCreatedResponse() throws Exception {
		var prescription = new MockMultipartFile(
			"prescription",
			"rx.pdf",
			"application/pdf",
			"test prescription".getBytes()
		);

		mockMvc.perform(
			multipart("/api/medicine-requests")
				.file(prescription)
				.param("name", "Rajesh Yadav")
				.param("phone", "9730086267")
				.param("medicineName", "Paracetamol 650")
				.param("address", "Ichalkaranji")
		)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value("NEW"))
			.andExpect(jsonPath("$.message").value("Medicine request received successfully."))
			.andExpect(jsonPath("$.prescriptionUploaded").value(true))
			.andExpect(jsonPath("$.requestId").isNotEmpty());

		var savedRequests = medicineRequestRepository.findAll();

		assertThat(savedRequests).hasSize(1);
		var savedRequest = savedRequests.get(0);
		assertThat(savedRequest.getName()).isEqualTo("Rajesh Yadav");
		assertThat(savedRequest.getPhone()).isEqualTo("9730086267");
		assertThat(savedRequest.getMedicineName()).isEqualTo("Paracetamol 650");
		assertThat(savedRequest.getAddress()).isEqualTo("Ichalkaranji");
		assertThat(savedRequest.getStatus()).isEqualTo(MedicineRequestStatus.NEW.name());
		assertThat(savedRequest.getPrescriptionFile()).isNotNull();
		assertThat(savedRequest.getPrescriptionFile().getOriginalFilename()).isEqualTo("rx.pdf");
		assertThat(savedRequest.getPrescriptionFile().getRequestId()).isEqualTo(savedRequest.getRequestId());
		assertThat(savedRequest.getPrescriptionFile().getSavedPath()).endsWith("/rx.pdf");
		assertThat(savedRequest.getPrescriptionFile().getUploadedAt()).isNotNull();

		try (var requestDirectories = Files.list(STORAGE_ROOT)) {
			var storedDirectories = requestDirectories.filter(Files::isDirectory).toList();

			assertThat(storedDirectories).hasSize(1);
			assertThat(Files.exists(storedDirectories.get(0).resolve("rx.pdf"))).isTrue();
		}
	}

	@Test
	void listsStoredMedicineRequestsNewestFirst() throws Exception {
		var firstRequestId = createMedicineRequest(
			"Rajesh Yadav",
			"9730086267",
			"Paracetamol 650",
			"Ichalkaranji",
			null
		);
		var secondRequestId = createMedicineRequest(
			"Sneha Patil",
			"9876543210",
			"Vitamin B12",
			"Kagwade Mala",
			null
		);

		mockMvc.perform(get("/api/medicine-requests"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].requestId").value(secondRequestId))
			.andExpect(jsonPath("$[0].name").value("Sneha Patil"))
			.andExpect(jsonPath("$[0].status").value("NEW"))
			.andExpect(jsonPath("$[0].prescriptionUploaded").value(false))
			.andExpect(jsonPath("$[1].requestId").value(firstRequestId))
			.andExpect(jsonPath("$[1].name").value("Rajesh Yadav"));
	}

	@Test
	void returnsMedicineRequestDetails() throws Exception {
		var prescription = new MockMultipartFile(
			"prescription",
			"rx.pdf",
			"application/pdf",
			"test prescription".getBytes()
		);
		var requestId = createMedicineRequest(
			"Rajesh Yadav",
			"9730086267",
			"Paracetamol 650",
			"Ichalkaranji",
			prescription
		);

		mockMvc.perform(get("/api/medicine-requests/{requestId}", requestId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.requestId").value(requestId))
			.andExpect(jsonPath("$.name").value("Rajesh Yadav"))
			.andExpect(jsonPath("$.address").value("Ichalkaranji"))
			.andExpect(jsonPath("$.status").value("NEW"))
			.andExpect(jsonPath("$.prescriptionFile.originalFilename").value("rx.pdf"))
			.andExpect(jsonPath("$.prescriptionFile.savedPath").value(requestId + "/rx.pdf"))
			.andExpect(jsonPath("$.prescriptionFile.uploadedAt").isNotEmpty());
	}

	@Test
	void updatesMedicineRequestStatus() throws Exception {
		var requestId = createMedicineRequest(
			"Rajesh Yadav",
			"9730086267",
			"Paracetamol 650",
			"Ichalkaranji",
			null
		);

		mockMvc.perform(
			patch("/api/medicine-requests/{requestId}/status", requestId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"status\":\"CONTACTED\"}")
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.requestId").value(requestId))
			.andExpect(jsonPath("$.status").value("CONTACTED"));

		var updatedRequest = medicineRequestRepository.findByRequestId(requestId).orElseThrow();
		assertThat(updatedRequest.getStatus()).isEqualTo(MedicineRequestStatus.CONTACTED.name());
	}

	@Test
	void returnsNotFoundForUnknownMedicineRequest() throws Exception {
		mockMvc.perform(get("/api/medicine-requests/{requestId}", "missing-request"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.status").value("NOT_FOUND"))
			.andExpect(jsonPath("$.message").value("Medicine request not found: missing-request"));
	}

	@Test
	void rejectsInvalidStatusUpdate() throws Exception {
		var requestId = createMedicineRequest(
			"Rajesh Yadav",
			"9730086267",
			"Paracetamol 650",
			"Ichalkaranji",
			null
		);

		mockMvc.perform(
			patch("/api/medicine-requests/{requestId}/status", requestId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"status\":\"DONE\"}")
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Status must be one of: NEW, CONTACTED, FULFILLED."));
	}

	@Test
	void rejectsMissingName() throws Exception {
		mockMvc.perform(
			multipart("/api/medicine-requests")
				.param("name", " ")
				.param("phone", "9730086267")
				.param("medicineName", "Paracetamol 650")
				.param("address", "Ichalkaranji")
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("Name is required."));
	}

	@Test
	void rejectsUnsupportedPrescriptionType() throws Exception {
		var prescription = new MockMultipartFile(
			"prescription",
			"rx.exe",
			"application/octet-stream",
			"test".getBytes()
		);

		mockMvc.perform(
			multipart("/api/medicine-requests")
				.file(prescription)
				.param("name", "Rajesh Yadav")
				.param("phone", "9730086267")
				.param("medicineName", "Paracetamol 650")
				.param("address", "Ichalkaranji")
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Prescription file must be a PDF, JPG, JPEG, or PNG."));
	}

	private String createMedicineRequest(
		String name,
		String phone,
		String medicineName,
		String address,
		MockMultipartFile prescription
	) throws Exception {
		var requestBuilder = multipart("/api/medicine-requests")
			.param("name", name)
			.param("phone", phone)
			.param("medicineName", medicineName)
			.param("address", address);

		if (prescription != null) {
			requestBuilder.file(prescription);
		}

		mockMvc.perform(requestBuilder)
			.andExpect(status().isCreated());

		return medicineRequestRepository.findAllByOrderByCreatedAtDescIdDesc()
			.get(0)
			.getRequestId();
	}

	private void deleteDirectory(Path directory) throws IOException {
		if (!Files.exists(directory)) {
			return;
		}

		try (var paths = Files.walk(directory)) {
			paths
				.sorted(Comparator.reverseOrder())
				.forEach(path -> {
					try {
						Files.deleteIfExists(path);
					} catch (IOException exception) {
						throw new RuntimeException(exception);
					}
				});
		}
	}
}
