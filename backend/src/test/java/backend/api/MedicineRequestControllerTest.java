package backend.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
			.andExpect(jsonPath("$.status").value("RECEIVED"))
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
