package com.magic.project.controller;

import com.magic.project.models.Patient;
import com.magic.project.services.PatientService;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("hospital/patient")
public class PatientController {
	@Autowired
	PatientService patServ;

	// Add a Patient
	@PostMapping("/add")
	public Mono<ServerResponse> addPatient(@Valid @RequestBody Mono<Patient> patientMono) {
		patientMono.flatMap(patient -> patServ.savePatient(patient));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(patientMono, Patient.class);
	}

	// delete a Patient
	@DeleteMapping("/delete/{patId}")
	public Mono<ServerResponse> deletePatient(@Valid @PathVariable String patId) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(patServ.deletePatient(patId), Patient.class);
	}

	// update a Patient by ID and Put request
	@PutMapping("/update/{patId}")
	public Mono<ServerResponse> updatePatient(@Valid @PathVariable String patId,
			@RequestBody Patient updatedPatient) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(patServ.updatePatient(updatedPatient, patId), Patient.class);
	}

	// get list of all Patients
	@GetMapping("/list")
	public Mono<ServerResponse>  getAllPatient() { 
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(patServ.getPatientList(), Patient.class);
	}
}
