package com.magic.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.models.Appointment;
import com.magic.project.services.AppointmentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("hospital/appointment")
public class AppointmentController {
	@Autowired
	AppointmentService appServ;

	// Add a Appointment
	@PostMapping("/add")
	public Mono<ServerResponse> addAppointment(@Valid @RequestBody Mono<Appointment> appointmentMono) {
		appointmentMono.flatMap(appointment -> appServ.saveAppointment(appointment));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(appointmentMono, Appointment.class);
	}

	// delete a Appointment
	@DeleteMapping("/delete/{appId}")
	public Mono<ServerResponse>  deleteAppointment(@Valid @PathVariable String appId) {
		Mono<Appointment> appointmentMono = appServ.deleteAppointment(appId);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(appointmentMono, Appointment.class);
	}

	// update a Appointment by ID and Put request
	@PutMapping("/update/{appId}")
	public Mono<ServerResponse> updateAppointment(@Valid @PathVariable String appId,
			@RequestBody Appointment updatedAppointment) {
		Mono<Appointment> appointmentMono = appServ.updateAppointment(updatedAppointment, appId);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(appointmentMono, Appointment.class);
	}

	// get list of all Appointments
	@GetMapping("/list")
	public Mono<ServerResponse> getAllAppointment() {
		Flux<Appointment> appointmentsFlux = appServ.getAppointmentList();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(appointmentsFlux, Appointment.class);
	}

	// update a Appointment Status by ID and Patch request
	@PatchMapping("/update/status/{appId}")
	public Mono<ServerResponse> updateAppointmentStaus(@Valid @PathVariable String appId,
			@RequestBody Appointment updatedAppointment) {
		Mono<Appointment> appointmentMono = appServ.updateAppointmentStatus(updatedAppointment, appId);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(appointmentMono, Appointment.class);
	}
}
