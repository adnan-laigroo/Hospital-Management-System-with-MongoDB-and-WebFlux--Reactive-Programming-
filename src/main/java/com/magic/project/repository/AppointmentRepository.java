package com.magic.project.repository;

import com.magic.project.models.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends  MongoRepository<Appointment, String> {

	List<Appointment> findByDocId(String email);

}