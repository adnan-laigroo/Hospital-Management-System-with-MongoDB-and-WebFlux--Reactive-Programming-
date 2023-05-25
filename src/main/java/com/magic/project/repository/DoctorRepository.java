package com.magic.project.repository;

import com.magic.project.models.Doctor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends ReactiveMongoRepository<Doctor, String> {

	List<Doctor> findBySpeciality(String speciality);

}
