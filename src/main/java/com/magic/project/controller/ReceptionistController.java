package com.magic.project.controller;

import com.magic.project.models.Receptionist;
import com.magic.project.models.User;
import com.magic.project.models.dto.ReceptionistDto;
import com.magic.project.models.dto.ReceptionistUserMapper;
import com.magic.project.services.ReceptionistService;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("hospital/receptionist")
public class ReceptionistController {
	@Autowired
	ReceptionistService recepServ;
	@Autowired
	UserService userServ;

	// Add a receptionist
	@PostMapping("/add")
	public Mono<ServerResponse> addReceptionist(@Valid @RequestBody Mono<ReceptionistDto> receptionistDtoMono) {
		return receptionistDtoMono.flatMap(receptionistDto -> {
			Receptionist receptionist = ReceptionistUserMapper.mapToReceptionist(receptionistDto);
			User user = ReceptionistUserMapper.mapToUser(receptionistDto);
			Mono<Receptionist> receptionistMono = recepServ.saveReceptionist(receptionist);
			user.setUsername(receptionist.getEmail());
			userServ.saveUser(user);
			return ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(receptionistMono,
					Receptionist.class);
		});
	}

	// delete a receptionist
	@DeleteMapping("/delete/{email}")
	public Mono<ServerResponse> deleteReceptionist(@Valid @PathVariable String email) {
		Mono<Receptionist> receptionistMono = recepServ.deleteReceptionist(email);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(receptionistMono, Receptionist.class);
	}

	// update a receptionist by ID and Put request
	@PutMapping("/update/{email}")
	public Mono<ServerResponse> updateReceptionist(@Valid @PathVariable String email,
			@RequestBody Receptionist updatedReceptionist) {
		Mono<Receptionist> receptionistMono = recepServ.updateReceptionist(updatedReceptionist, email);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(receptionistMono, Receptionist.class);
	}

	// get list of all receptionists
	@GetMapping("/list")
	public Mono<ServerResponse> getAllReceptionist() {
		Flux<Receptionist> receptionistsFlux = recepServ.getReceptionistList();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(receptionistsFlux, Receptionist.class);
	}

}
