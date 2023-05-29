package com.magic.project.handler;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.models.Doctor;
import com.magic.project.models.User;
import com.magic.project.models.dto.DoctorDto;
import com.magic.project.models.dto.DoctorUserMapper;
import com.magic.project.services.DoctorService;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DoctorHandler {

	@Autowired
	private DoctorService doctorService;
	@Autowired
	private UserService userService;

	public Mono<ServerResponse> addDoctor(ServerRequest  request) {
		Mono<@Valid DoctorDto> doctorDtoMono = request.bodyToMono(DoctorDto.class);
		return doctorDtoMono.flatMap(doctorDto -> {
			@Valid Doctor doctor = DoctorUserMapper.mapToDoctor(doctorDto);
			@Valid User user = DoctorUserMapper.mapToUser(doctorDto);
			Mono<Doctor> doctorMono = doctorService.saveDoctor(doctor);
			user.setUsername(doctor.getEmail());
			userService.saveUser(user);
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(doctorMono, Doctor.class);
		});
	}

	public Mono<ServerResponse> deleteDoctor(ServerRequest request) {
		String email = request.pathVariable("email");
		Mono<Doctor> doctorMono = doctorService.deleteDoctor(email);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(doctorMono, Doctor.class);
	}

	public Mono<ServerResponse> updateDoctor(ServerRequest request) {
		String email = request.pathVariable("email");
		Mono<Doctor> updatedDoctorMono = request.bodyToMono(Doctor.class)
				.flatMap(updatedDoctor -> doctorService.updateDoctor(updatedDoctor, email));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(updatedDoctorMono, Doctor.class);
	}

	public Mono<ServerResponse> getAllDoctors(ServerRequest request) {
		Flux<Doctor> doctorsFlux = doctorService.getDoctorList();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(doctorsFlux, Doctor.class);
	}
}
