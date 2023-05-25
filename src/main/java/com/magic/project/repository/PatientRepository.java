package com.magic.project.repository;

import com.magic.project.models.Patient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends ReactiveMongoRepository<Patient, String> {

}
