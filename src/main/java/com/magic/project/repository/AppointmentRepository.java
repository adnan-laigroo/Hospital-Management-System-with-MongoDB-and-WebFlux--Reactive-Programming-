package com.magic.project.repository;

import com.magic.project.models.Appointment;
import com.magic.project.models.Doctor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends ReactiveMongoRepository<Appointment, String> {

	Flux<Appointment> findByDocId(String email);

	Mono<Appointment> countByDocIdAndAppointmentStatus(String email, String string);

	@Query("SELECT a FROM Appointment a WHERE a.docId = :docId AND a.appointmentDate = :appointmentDate "
			+ "AND a.appointmentTime = :appointmentTime AND a.appointmentStatus = :appointmentStatus")
	Flux<Appointment> findByDocIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatus(@Param("docId") String docId,
			@Param("appointmentDate") LocalDate appointmentDate, @Param("appointmentTime") LocalTime appointmentTime,
			@Param("appointmentStatus") String appointmentStatus);

	Mono<Appointment> findFirstByOrderByApIdDesc();

	@Query(value = "{'apId': { $regex: '^A[0-9]{5}$' }}", sort = "{'apId': -1}", fields = "{'apId': 1}")
	Mono<String> findLastApId();

	Flux<Doctor> findByDocIdAndAppointmentDateAndAppointmentTime(String email, LocalDate appointmentDate,
			String appointmentTime);

}