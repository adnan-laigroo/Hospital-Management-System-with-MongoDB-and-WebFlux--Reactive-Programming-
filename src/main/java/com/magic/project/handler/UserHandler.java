package com.magic.project.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.models.Password;
import com.magic.project.models.User;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	@Autowired
	private UserService userService;

	public Mono<ServerResponse> updateUserPassword(ServerRequest request) {
		String username = request.pathVariable("username");
		Mono<Password> updatedPasswordMono = request.bodyToMono(Password.class);
		Mono<User> userMono = updatedPasswordMono
				.flatMap(updatedPassword -> userService.updateUserPassword(updatedPassword, username));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userMono, User.class);
	}

	public Mono<ServerResponse> getAllUsers(ServerRequest request) {
		Flux<User> usersFlux = userService.getUserList();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(usersFlux, User.class);
	}
}
