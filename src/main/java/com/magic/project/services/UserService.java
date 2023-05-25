package com.magic.project.services;

import com.magic.project.models.Password;
import com.magic.project.models.User;

import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.util.List;

public interface UserService {
	User updateUserPassword(@Valid Password updatedPassword, @Valid String username);

	Mono<User> saveUser(User user);

	List<User> getUserList();

}