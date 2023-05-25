package com.magic.project.controller;

import com.magic.project.models.Doctor;
import com.magic.project.models.User;
import com.magic.project.models.dto.DoctorDto;
import com.magic.project.models.dto.DoctorUserMapper;
import com.magic.project.services.DoctorService;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


@RestController
@RequestMapping("hospital/doctor")
public class DoctorController {
	
	@Autowired
	DoctorService docServ;
	
	@Autowired
	UserService userServ;

	// Add an doctor
	@PostMapping("/add")
	public Mono<ServerResponse> addDoctor(ServerRequest request, @Valid Mono<DoctorDto> doctorDtoMono) {
	    return doctorDtoMono.flatMap(doctorDto -> {
	        Doctor doctor = DoctorUserMapper.mapToDoctor(doctorDto);
	        User user = DoctorUserMapper.mapToUser(doctorDto);
	        Mono<Doctor> doctorMono = docServ.saveDoctor(doctor);
	        user.setUsername(doctor.getEmail());
	        userServ.saveUser(user);
	        return ServerResponse.ok()
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(doctorMono, Doctor.class);
	    });
	}
	
	// delete a doctor
	@DeleteMapping("/delete/{email}")
	public Mono<ServerResponse> deleteDoctor(@Valid @PathVariable String email) {
	    Mono<Doctor> doctorMono = docServ.deleteDoctor(email);
	    return ServerResponse.ok()
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(doctorMono, Doctor.class);
	}


	// update a doctor by ID and Put request
	@PutMapping("/update/{email}")
	public Mono<ServerResponse> updateDoctor(@Valid @PathVariable String email, @Valid @RequestBody Mono<Doctor> updatedDoctorMono) {
		updatedDoctorMono = updatedDoctorMono
				.flatMap(updatedDoctor -> docServ.updateDoctor(updatedDoctor, email));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(updatedDoctorMono, Doctor.class);
	}

	// get list of all doctors
	@GetMapping("/list")
	public Mono<ServerResponse> getAllDoctor() {
		Flux<Doctor> doctorsFlux = docServ.getDoctorList();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(doctorsFlux, Doctor.class);
	}

}