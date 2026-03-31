package backend.api;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import backend.medicinerequest.MedicineRequestNotFoundException;
import backend.medicinerequest.MedicineRequestStorageException;
import backend.medicinerequest.PrescriptionFileTooLargeException;
import backend.medicinerequest.RateLimitExceededException;

@RestControllerAdvice
public class ApiExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationFailure(BindException exception) {
		var message = exception.getBindingResult().getFieldErrors().stream()
			.findFirst()
			.map(error -> error.getDefaultMessage())
			.filter(value -> value != null && !value.isBlank())
			.orElse("Request is invalid.");

		return ResponseEntity.badRequest().body(
			new ApiErrorResponse("BAD_REQUEST", message, Instant.now())
		);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException exception) {
		return ResponseEntity.badRequest().body(
			new ApiErrorResponse("BAD_REQUEST", exception.getMessage(), Instant.now())
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(
		HttpMessageNotReadableException exception
	) {
		return ResponseEntity.badRequest().body(
			new ApiErrorResponse("BAD_REQUEST", "Request body is invalid.", Instant.now())
		);
	}

	@ExceptionHandler(PrescriptionFileTooLargeException.class)
	public ResponseEntity<ApiErrorResponse> handleOversizedPrescription(
		PrescriptionFileTooLargeException exception
	) {
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
			new ApiErrorResponse("PAYLOAD_TOO_LARGE", exception.getMessage(), Instant.now())
		);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiErrorResponse> handleMultipartLimitExceeded(
		MaxUploadSizeExceededException exception
	) {
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
			new ApiErrorResponse(
				"PAYLOAD_TOO_LARGE",
				"Prescription file is too large.",
				Instant.now()
			)
		);
	}

	@ExceptionHandler(RateLimitExceededException.class)
	public ResponseEntity<ApiErrorResponse> handleRateLimitExceeded(
		RateLimitExceededException exception
	) {
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
			new ApiErrorResponse("TOO_MANY_REQUESTS", exception.getMessage(), Instant.now())
		);
	}

	@ExceptionHandler(MedicineRequestStorageException.class)
	public ResponseEntity<ApiErrorResponse> handleStorageError(
		MedicineRequestStorageException exception
	) {
		logger.error("Could not store medicine request.", exception);
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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpectedFailure(Exception exception) {
		logger.error("Unexpected API failure.", exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
			new ApiErrorResponse(
				"ERROR",
				"Something went wrong on the server. Please try again later.",
				Instant.now()
			)
		);
	}
}
