package com.magic.project.services.implementation;

import com.magic.project.exceptionHandler.PatientNotFoundException;
import com.magic.project.models.Patient;
import com.magic.project.repository.PatientRepository;
import com.magic.project.services.PatientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class PatientServiceImplementation implements PatientService {
	@Autowired
	PatientRepository patRepo;

	@Override
	public Mono<Patient> savePatient(@Valid Patient Patient) {
		return patRepo.save(Patient);
	}

	@Override
	public Mono<Patient> deletePatient(@Valid String patId) {
		return patRepo.findById(patId)
				.switchIfEmpty(Mono.error(new PatientNotFoundException("No Patient with ID " + patId)))
				.flatMap(patient -> patRepo.deleteById(patId).thenReturn(patient));
	}

	@Override
	public Mono<Patient> updatePatient(Patient updatedPatient, @Valid String patId) {
		return patRepo.findById(patId)
				.switchIfEmpty(Mono.error(new PatientNotFoundException("No Patient with ID " + patId)))
				.flatMap(patient -> {
					updatedPatient.setPatId(patId);
					return patRepo.save(updatedPatient);
				});
	}

	@Override
	public Flux<Patient> getPatientList() {
		Flux<Patient> patients = patRepo.findAll();
		return patients
				.switchIfEmpty(Mono.error(new PatientNotFoundException("No Patient Found")));
	}
}