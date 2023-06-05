package com.magic.project.services;

import com.magic.project.models.Appointment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface AppointmentService {

	Mono<Appointment> saveAppointment(@Valid Appointment appointment);

	Mono<Appointment> deleteAppointment(@Valid String appId);

	Mono<Appointment> updateAppointment(Appointment updatedAppointment, @Valid String appId);
	
	Flux<Appointment> getAppointmentList();

	Mono<Appointment> updateAppointmentStatus(Appointment updatedAppointment, @Valid String appId);

	Mono<Appointment> saveFirstAppointment(@Valid Appointment appointment);

	Mono<Appointment> saveOtherAppointment(@Valid Appointment appointment);

	Mono<Appointment> getAppointment(String appId);

}