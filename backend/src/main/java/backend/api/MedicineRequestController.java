package backend.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
