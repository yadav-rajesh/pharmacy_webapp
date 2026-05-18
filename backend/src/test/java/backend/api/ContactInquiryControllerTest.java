package backend.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import backend.persistence.repository.ContactInquiryRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
	"spring.datasource.url=jdbc:h2:mem:contact-inquiry-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.hibernate.ddl-auto=create-drop",
	"medicine-requests.rate-limit.enabled=false",
	"backend.cors.allowed-origins=http://localhost:5173,http://127.0.0.1:5173"
})
class ContactInquiryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ContactInquiryRepository contactInquiryRepository;

	@BeforeEach
	void clearState() {
		contactInquiryRepository.deleteAll();
	}

	@Test
	void storesContactInquiryAndReturnsCreatedResponse() throws Exception {
		mockMvc.perform(
			post("/api/contact-inquiries")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "name": "Rajesh Yadav",
					  "phone": "9730086267",
					  "message": "Please call me back about regular medicines."
					}
					""")
		)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value("NEW"))
			.andExpect(jsonPath("$.message").value("Contact inquiry received successfully."))
			.andExpect(jsonPath("$.inquiryId").isNotEmpty());

		var savedInquiries = contactInquiryRepository.findAll();
		assertThat(savedInquiries).hasSize(1);

		var savedInquiry = savedInquiries.get(0);
		assertThat(savedInquiry.getName()).isEqualTo("Rajesh Yadav");
		assertThat(savedInquiry.getPhone()).isEqualTo("9730086267");
		assertThat(savedInquiry.getMessage()).isEqualTo("Please call me back about regular medicines.");
		assertThat(savedInquiry.getStatus()).isEqualTo("NEW");
		assertThat(savedInquiry.getInquiryId()).isNotBlank();
		assertThat(savedInquiry.getCreatedAt()).isNotNull();
	}

	@Test
	void rejectsInvalidPhoneNumber() throws Exception {
		mockMvc.perform(
			post("/api/contact-inquiries")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "name": "Rajesh Yadav",
					  "phone": "abc123",
					  "message": "Need help."
					}
					""")
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("Phone number must contain 7 to 20 valid characters."));
	}

	@Test
	void rejectsMissingMessage() throws Exception {
		mockMvc.perform(
			post("/api/contact-inquiries")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "name": "Rajesh Yadav",
					  "phone": "9730086267",
					  "message": " "
					}
					""")
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("Message is required."));
	}

	@Test
	void allowsCorsPreflightFromConfiguredOrigin() throws Exception {
		mockMvc.perform(
			options("/api/contact-inquiries")
				.header("Origin", "http://localhost:5173")
				.header("Access-Control-Request-Method", "POST")
				.header("Access-Control-Request-Headers", "content-type")
		)
			.andExpect(status().isOk())
			.andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
	}
}

