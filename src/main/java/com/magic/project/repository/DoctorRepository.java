package com.magic.project.repository;

import com.magic.project.models.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

	List<Doctor> findBySpeciality(String speciality);

}
