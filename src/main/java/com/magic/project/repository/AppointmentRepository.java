package com.magic.project.repository;

import com.magic.project.models.Appointment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends ReactiveMongoRepository<Appointment, String> {

	Flux<Appointment> findByDocId(String email);

	Mono<Appointment> countByDocIdAndAppointmentStatus(String email, String string);

}