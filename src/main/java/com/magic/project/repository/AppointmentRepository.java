package com.magic.project.repository;

import com.magic.project.models.Appointment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends ReactiveMongoRepository<Appointment, String> {

	List<Appointment> findByDocId(String email);

}