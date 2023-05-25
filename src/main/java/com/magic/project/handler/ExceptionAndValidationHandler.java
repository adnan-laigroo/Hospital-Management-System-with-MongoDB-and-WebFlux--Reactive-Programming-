package com.magic.project.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionAndValidationHandler implements WebExceptionHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		if (ex instanceof MethodArgumentNotValidException) {
			return handleMethodArgumentNotValid((MethodArgumentNotValidException) ex, exchange);
		} else if (ex instanceof PatientNotFoundException) {
			return handlePatientNotFoundException((PatientNotFoundException) ex, exchange);
		} else if (ex instanceof DoctorNotFoundException) {
			return handleDoctorNotFoundException((DoctorNotFoundException) ex, exchange);
		} else if (ex instanceof ReceptionistNotFoundException) {
			return handleReceptionistNotFoundException((ReceptionistNotFoundException) ex, exchange);
		} else if (ex instanceof UserNotFoundException) {
			return handleUserNotFoundException((UserNotFoundException) ex, exchange);
		} else if (ex instanceof AppointmentNotConfirmedException) {
			return handleAppointmentException((AppointmentNotConfirmedException) ex, exchange);
		} else {
			// Handle other exceptions or return a default error response
			ResponseError response = new ResponseError("Internal Server Error");
			return handleErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, exchange);
		}
	}

	private Mono<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, ServerWebExchange exchange) {
		BindingResult br = ex.getBindingResult();
		List<ObjectError> errors = br.getAllErrors();
		List<String> errorMessages = new ArrayList<>();
		for (ObjectError error : errors) {
			if (error instanceof FieldError) {
				FieldError fieldError = (FieldError) error;
				String errorMessage = fieldError.getDefaultMessage();
				errorMessages.add(errorMessage);
			}
		}
		if (errorMessages.isEmpty()) {
			errorMessages.add("Invalid request payload");
		}
		ResponseError response = new ResponseError("Validation Failed", errorMessages);
		return handleErrorResponse(response, HttpStatus.BAD_REQUEST, exchange);
	}

	private Mono<Void> handlePatientNotFoundException(PatientNotFoundException ex, ServerWebExchange exchange) {
		List<String> errorMessages = new ArrayList<>();
		errorMessages.add(ex.getMessage());
		ResponseError response = new ResponseError("Patient Not Found", errorMessages);
		return handleErrorResponse(response, HttpStatus.NOT_FOUND, exchange);
	}

	private Mono<Void> handleDoctorNotFoundException(DoctorNotFoundException ex, ServerWebExchange exchange) {
		List<String> errorMessages = new ArrayList<>();
		errorMessages.add(ex.getMessage());
		ResponseError response = new ResponseError("Doctor Not Found", errorMessages);
		return handleErrorResponse(response, HttpStatus.NOT_FOUND, exchange);
	}

	private Mono<Void> handleReceptionistNotFoundException(ReceptionistNotFoundException ex,
			ServerWebExchange exchange) {
		List<String> errorMessages = new ArrayList<>();
		errorMessages.add(ex.getMessage());
		ResponseError response = new ResponseError("Receptionist Not Found", errorMessages);
		return handleErrorResponse(response, HttpStatus.NOT_FOUND, exchange);
	}

	private Mono<Void> handleUserNotFoundException(UserNotFoundException ex, ServerWebExchange exchange) {
		List<String> errorMessages = new ArrayList<>();
		errorMessages.add(ex.getMessage());
		ResponseError response = new ResponseError("User Not Found", errorMessages);
		return handleErrorResponse(response, HttpStatus.NOT_FOUND, exchange);
	}

	private Mono<Void> handleAppointmentException(AppointmentNotConfirmedException ex, ServerWebExchange exchange) {
		List<String> errorMessages = new ArrayList<>();
		errorMessages.add(ex.getMessage());
		ResponseError response = new ResponseError("Appointment Not Confirmed", errorMessages);
		return handleErrorResponse(response, HttpStatus.NOT_FOUND, exchange);
	}

//	private Mono<Void> handleUsernameNotFoundException(UsernameNotFoundException ex, ServerWebExchange exchange) {
//		List<String> errorMessages = new ArrayList<>();
//		errorMessages.add(ex.getMessage());
//		ResponseError response = new ResponseError("Username Not Found Exception", errorMessages);
//		return handleErrorResponse(response, HttpStatus.NOT_FOUND, exchange);
//	}

	private Mono<Void> handleErrorResponse(ResponseError response, HttpStatus status, ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		return exchange.getResponse()
				.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(response.toString().getBytes())));
	}

}
