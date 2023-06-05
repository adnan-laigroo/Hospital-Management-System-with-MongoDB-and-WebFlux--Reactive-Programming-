package com.magic.project.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.models.Appointment;
import com.magic.project.models.Patient;
import com.magic.project.services.PatientService;
import reactor.core.publisher.Mono;

@Component
public class PatientHandler {

	@Autowired
	private PatientService patientService;

	public Mono<ServerResponse> addPatient(ServerRequest request) {
		Mono<Patient> patientMono = request.bodyToMono(Patient.class)
				.flatMap(patient -> patientService.savePatient(patient));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(patientMono, Patient.class);
	}

	public Mono<ServerResponse> deletePatient(ServerRequest request) {
		String patId = request.pathVariable("patId");
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(patientService.deletePatient(patId),
				Patient.class);
	}

	public Mono<ServerResponse> updatePatient(ServerRequest request) {
		String patId = request.pathVariable("patId");
		Mono<Patient> updatedPatientMono = request.bodyToMono(Patient.class)
				.flatMap(updatedPatient -> patientService.updatePatient(updatedPatient, patId));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(updatedPatientMono, Patient.class);
	}

	public Mono<ServerResponse> getAllPatients(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(patientService.getPatientList(),
				Patient.class);
	}
	public Mono<ServerResponse> getPatientById(ServerRequest request) {
		String patId = request.pathVariable("patId");
		Mono<Patient> patientMono = patientService.getPatient(patId);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(patientMono, Patient.class);
	}
}
