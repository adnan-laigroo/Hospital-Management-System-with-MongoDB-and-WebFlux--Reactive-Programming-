package com.magic.project.repository;

import com.magic.project.models.Doctor;

import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends ReactiveMongoRepository<Doctor, String> {

	Flux<Doctor> findBySpeciality(String speciality);

}
  