package com.magic.project.services;

import com.magic.project.models.Doctor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

public interface DoctorService {

	Mono<Doctor> saveDoctor(@Valid Doctor doctor);

	Mono<Doctor> deleteDoctor(@Valid String email);

	Mono<Doctor> updateDoctor(Doctor updatedDoctor, @Valid String email);

	Flux<Doctor> getDoctorList();

	Mono<Doctor> getDoctor(String email);

}