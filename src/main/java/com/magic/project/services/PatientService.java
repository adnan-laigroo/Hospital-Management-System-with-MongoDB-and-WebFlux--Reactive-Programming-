package com.magic.project.services;

import com.magic.project.models.Patient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface PatientService {

	Mono<Patient> savePatient(@Valid Patient patient);

	Mono<Patient> deletePatient(@Valid String patId);

	Mono<Patient> updatePatient(Patient updatedPatient, @Valid String patId);

	Flux<Patient> getPatientList();

}