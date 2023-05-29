package com.magic.project.services.implementation;

import com.magic.project.exceptionHandler.DoctorNotFoundException;
import com.magic.project.models.Doctor;
import com.magic.project.repository.DoctorRepository;
import com.magic.project.services.DoctorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class DoctorServiceImplementation implements DoctorService {
	@Autowired
	DoctorRepository docRepo;

	@Override
	public Mono<Doctor> saveDoctor(@Valid Doctor doctor) {
		return docRepo.save(doctor);
	}

	@Override
	public Mono<Doctor> deleteDoctor(@Valid String email) {
		return docRepo.findById(email)
				.switchIfEmpty(Mono.error(new DoctorNotFoundException("No doctor found with ID " + email)))
				.flatMap(doctor -> docRepo.deleteById(email).thenReturn(doctor));
				
	}

	@Override
	public Mono<Doctor> updateDoctor(Doctor updatedDoctor, @Valid String email) {
		return docRepo.findById(email)
				.switchIfEmpty(Mono.error(new DoctorNotFoundException("No doctor found with ID " + email)))
				.flatMap(doctor -> {
					updatedDoctor.setEmail(email);
					return docRepo.save(updatedDoctor);
				});
	}

	@Override
	public Flux<Doctor> getDoctorList() {
		Flux<Doctor> doctors = docRepo.findAll();
		return doctors.switchIfEmpty(Mono.error(new DoctorNotFoundException("No doctor found.")));
	}
}