package backend.api;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import backend.medicinerequest.MedicineRequestNotFoundException;
import backend.medicinerequest.MedicineRequestStorageException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException exception) {
		return ResponseEntity.badRequest().body(
			new ApiErrorResponse("BAD_REQUEST", exception.getMessage(), Instant.now())
		);
	}

	@ExceptionHandler(MedicineRequestStorageException.class)
	public ResponseEntity<ApiErrorResponse> handleStorageError(
		MedicineRequestStorageException exception
	) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
			new ApiErrorResponse(
				"ERROR",
				"Medicine request could not be stored right now. Please try again.",
				Instant.now()
			)
		);
	}

	@ExceptionHandler(MedicineRequestNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(
		MedicineRequestNotFoundException exception
	) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new ApiErrorResponse("NOT_FOUND", exception.getMessage(), Instant.now())
		);
	}
}
