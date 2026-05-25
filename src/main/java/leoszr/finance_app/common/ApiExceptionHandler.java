package leoszr.finance_app.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception,
			HttpServletRequest request
	) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));

		return build(HttpStatus.BAD_REQUEST, ApiErrorCode.VALIDATION_ERROR, message, request);
	}

	@ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
	public ResponseEntity<ApiErrorResponse> handleValidation(Exception exception, HttpServletRequest request) {
		return build(HttpStatus.BAD_REQUEST, ApiErrorCode.VALIDATION_ERROR, exception.getMessage(), request);
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessRule(BusinessRuleException exception, HttpServletRequest request) {
		return build(HttpStatus.BAD_REQUEST, ApiErrorCode.BUSINESS_RULE_ERROR, exception.getMessage(), request);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException exception, HttpServletRequest request) {
		return build(HttpStatus.NOT_FOUND, ApiErrorCode.NOT_FOUND, exception.getMessage(), request);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException exception, HttpServletRequest request) {
		return build(HttpStatus.UNAUTHORIZED, ApiErrorCode.UNAUTHORIZED, exception.getMessage(), request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, ApiErrorCode.INTERNAL_ERROR, "Erro interno.", request);
	}

	private ResponseEntity<ApiErrorResponse> build(
			HttpStatus status,
			ApiErrorCode error,
			String message,
			HttpServletRequest request
	) {
		ApiErrorResponse response = new ApiErrorResponse(
				Instant.now(),
				status.value(),
				error,
				message,
				request.getRequestURI()
		);

		return ResponseEntity.status(status).body(response);
	}
}
