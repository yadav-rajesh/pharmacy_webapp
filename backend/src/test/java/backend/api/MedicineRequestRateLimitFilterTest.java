package backend.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
	"medicine-requests.storage-dir=target/rate-limit-test-requests",
	"spring.datasource.url=jdbc:h2:mem:medicine-request-rate-limit-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.hibernate.ddl-auto=create-drop",
	"medicine-requests.rate-limit.enabled=true",
	"medicine-requests.rate-limit.max-requests=2",
	"medicine-requests.rate-limit.window-seconds=60"
})
class MedicineRequestRateLimitFilterTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void rejectsRequestsWhenRateLimitIsExceeded() throws Exception {
		for (var attempt = 0; attempt < 2; attempt++) {
			mockMvc.perform(
				multipart("/api/medicine-requests")
					.param("name", "Rajesh Yadav")
					.param("phone", "9730086267")
					.param("medicineName", "Paracetamol 650")
					.param("address", "Ichalkaranji")
					.with(request -> {
						request.setRemoteAddr("198.51.100.10");
						return request;
					})
			)
				.andExpect(status().isCreated());
		}

		mockMvc.perform(
			multipart("/api/medicine-requests")
				.param("name", "Rajesh Yadav")
				.param("phone", "9730086267")
				.param("medicineName", "Paracetamol 650")
				.param("address", "Ichalkaranji")
				.with(request -> {
					request.setRemoteAddr("198.51.100.10");
					return request;
				})
		)
			.andExpect(status().isTooManyRequests())
			.andExpect(jsonPath("$.status").value("TOO_MANY_REQUESTS"))
			.andExpect(jsonPath("$.message").value(
				"Too many medicine requests from this address. Please try again shortly."
			));
	}
}
