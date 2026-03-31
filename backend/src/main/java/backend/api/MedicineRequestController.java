package backend.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import backend.medicinerequest.CreateMedicineRequest;
import backend.medicinerequest.MedicineRequestService;

@RestController
@RequestMapping("/api/medicine-requests")
public class MedicineRequestController {

	private final MedicineRequestService medicineRequestService;

	public MedicineRequestController(MedicineRequestService medicineRequestService) {
		this.medicineRequestService = medicineRequestService;
	}

	@GetMapping
	public List<MedicineRequestSummaryResponse> getMedicineRequests() {
		return medicineRequestService.list();
	}

	@GetMapping("/{requestId}")
	public MedicineRequestDetailResponse getMedicineRequest(@PathVariable String requestId) {
		return medicineRequestService.get(requestId);
	}

	@GetMapping("/{requestId}/integration-preview")
	public MedicineRequestIntegrationPreviewResponse getMedicineRequestIntegrationPreview(
		@PathVariable String requestId
	) {
		return medicineRequestService.getIntegrationPreview(requestId);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MedicineRequestResponse> createMedicineRequest(
		@RequestParam String name,
		@RequestParam String phone,
		@RequestParam String medicineName,
		@RequestParam String address,
		@RequestParam(required = false) MultipartFile prescription
	) {
		var response = medicineRequestService.create(
			new CreateMedicineRequest(name, phone, medicineName, address),
			prescription
		);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping(path = "/{requestId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
	public MedicineRequestDetailResponse updateMedicineRequestStatus(
		@PathVariable String requestId,
		@RequestBody UpdateMedicineRequestStatusRequest request
	) {
		return medicineRequestService.updateStatus(requestId, request.status());
	}
}
