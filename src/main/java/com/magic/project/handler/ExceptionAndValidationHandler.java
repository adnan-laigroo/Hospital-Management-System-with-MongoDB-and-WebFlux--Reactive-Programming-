package com.magic.project.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionAndValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
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
        return Mono.just(response);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseError> handlePatientNotFoundException(PatientNotFoundException ex) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ex.getMessage());
        ResponseError response = new ResponseError("Patient Not Found", errorMessages);
        return Mono.just(response);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseError> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ex.getMessage());
        ResponseError response = new ResponseError("Doctor Not Found", errorMessages);
        return Mono.just(response);
    }

    @ExceptionHandler(ReceptionistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseError> handleReceptionistNotFoundException(ReceptionistNotFoundException ex) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ex.getMessage());
        ResponseError response = new ResponseError("Receptionist Not Found", errorMessages);
        return Mono.just(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseError> handleUserNotFoundException(UserNotFoundException ex) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ex.getMessage());
        ResponseError response = new ResponseError("User Not Found", errorMessages);
        return Mono.just(response);
    }

    @ExceptionHandler(AppointmentNotConfirmedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseError> handleAppointmentException(AppointmentNotConfirmedException ex) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ex.getMessage());
        ResponseError response = new ResponseError("Appointment Not Confirmed", errorMessages);
        return Mono.just(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseError> handleException(Exception ex) {
        ResponseError response = new ResponseError("Internal Server Error");
        return Mono.just(response);
    }

    private Mono<Void> handleErrorResponse(ResponseError response, HttpStatus status, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory()
                .wrap(response.toString().getBytes())));
    }
}
