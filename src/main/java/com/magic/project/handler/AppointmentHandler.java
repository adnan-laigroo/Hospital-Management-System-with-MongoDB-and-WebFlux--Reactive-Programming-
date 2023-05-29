package com.magic.project.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.models.Appointment;
import com.magic.project.services.AppointmentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class AppointmentHandler {
	
	@Autowired
	private AppointmentService appointmentService;

	public Mono<ServerResponse> addAppointment(ServerRequest request) {
		Mono<Appointment> appointmentMono = request.bodyToMono(Appointment.class)
				.flatMap(appointment -> appointmentService.saveAppointment(appointment));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(appointmentMono, Appointment.class);
	}

	public Mono<ServerResponse> deleteAppointment(ServerRequest request) {
		String appId = request.pathVariable("appId");
		Mono<Appointment> appointmentMono = appointmentService.deleteAppointment(appId);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(appointmentMono, Appointment.class);
	}

	public Mono<ServerResponse> updateAppointment(ServerRequest request) {
		String appId = request.pathVariable("appId");
		Mono<Appointment> updatedAppointmentMono = request.bodyToMono(Appointment.class)
				.flatMap(updatedAppointment -> appointmentService.updateAppointment(updatedAppointment, appId));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(updatedAppointmentMono,
				Appointment.class);
	}

	public Mono<ServerResponse> getAllAppointments(ServerRequest request) {
		Flux<Appointment> appointmentsFlux = appointmentService.getAppointmentList();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(appointmentsFlux, Appointment.class);
	}

	public Mono<ServerResponse> updateAppointmentStatus(ServerRequest request) {
		String appId = request.pathVariable("appId");
		Mono<Appointment> updatedAppointmentMono = request.bodyToMono(Appointment.class)
				.flatMap(updatedAppointment -> appointmentService.updateAppointmentStatus(updatedAppointment, appId));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(updatedAppointmentMono,
				Appointment.class);
	}
}
