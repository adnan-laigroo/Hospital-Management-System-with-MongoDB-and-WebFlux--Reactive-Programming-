package com.magic.project.repository;

import com.magic.project.models.Appointment;
import com.magic.project.models.Patient;

import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends ReactiveMongoRepository<Patient, String> {

	Flux<Appointment> findAllByPatId(String patId);

}
