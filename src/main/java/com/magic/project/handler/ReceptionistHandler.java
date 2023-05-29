package com.magic.project.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.models.Receptionist;
import com.magic.project.models.User;
import com.magic.project.models.dto.ReceptionistDto;
import com.magic.project.models.dto.ReceptionistUserMapper;
import com.magic.project.services.ReceptionistService;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReceptionistHandler {
	@Autowired
	private ReceptionistService receptionistService;
	@Autowired
	private UserService userService;

	public Mono<ServerResponse> addReceptionist(ServerRequest request) {
		Mono<ReceptionistDto> receptionistDtoMono = request.bodyToMono(ReceptionistDto.class);
		return receptionistDtoMono.flatMap(receptionistDto -> {
			Receptionist receptionist = ReceptionistUserMapper.mapToReceptionist(receptionistDto);
			User user = ReceptionistUserMapper.mapToUser(receptionistDto);
			Mono<Receptionist> receptionistMono = receptionistService.saveReceptionist(receptionist);
			user.setUsername(receptionist.getEmail());
			userService.saveUser(user);
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(receptionistMono,
					Receptionist.class);
		});
	}

	public Mono<ServerResponse> deleteReceptionist(ServerRequest request) {
		String email = request.pathVariable("email");
		Mono<Receptionist> receptionistMono = receptionistService.deleteReceptionist(email);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(receptionistMono, Receptionist.class);
	}

	public Mono<ServerResponse> updateReceptionist(ServerRequest request) {
		String email = request.pathVariable("email");
		Mono<Receptionist> updatedReceptionistMono = request.bodyToMono(Receptionist.class)
				.flatMap(updatedReceptionist -> receptionistService.updateReceptionist(updatedReceptionist, email));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(updatedReceptionistMono,
				Receptionist.class);
	}

	public Mono<ServerResponse> getAllReceptionists(ServerRequest request) {
		Flux<Receptionist> receptionistsFlux = receptionistService.getReceptionistList();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(receptionistsFlux, Receptionist.class);
	}
}
