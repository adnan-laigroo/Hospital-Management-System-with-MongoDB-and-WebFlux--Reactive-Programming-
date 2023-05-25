package com.magic.project.controller;

import com.magic.project.models.Password;
import com.magic.project.models.User;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("hospital/user")
public class UserController {
	@Autowired
	UserService userServ;

	// update a user password by ID and Patch request
	@PatchMapping("/update/password/{username}")
	public Mono<ServerResponse> updateUserPassword(@Valid @PathVariable String username,
	        @Valid @RequestBody Mono<Password> updatedPasswordMono) {
	    Mono<User> userMono = updatedPasswordMono
	            .flatMap(updatedPassword -> userServ.updateUserPassword(updatedPassword, username));
	    return ServerResponse.ok()
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(userMono, User.class);
	}


	// get list of all user
	@GetMapping("/list")
	public Mono<ServerResponse> getAllUser() {
		Flux<User> usersFlux = userServ.getUserList();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(usersFlux, User.class);
	}

}
