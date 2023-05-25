package com.magic.project.services;

import com.magic.project.models.Password;
import com.magic.project.models.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface UserService {
	
	Mono<User> updateUserPassword(@Valid Password updatedPassword, @Valid String username);

	Mono<User> saveUser(User user);

	Flux<User> getUserList();

}