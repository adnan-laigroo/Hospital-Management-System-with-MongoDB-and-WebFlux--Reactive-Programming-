package com.magic.project.services.implementation;

import com.magic.project.handler.UserNotFoundException;
import com.magic.project.models.Password;
import com.magic.project.models.User;
import com.magic.project.repository.UserDRepository;
import com.magic.project.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	UserDRepository userRepo;

	@Override
	public Mono<User> updateUserPassword(@Valid Password updatedPassword, String username) {
		return userRepo.findById(username)
				.switchIfEmpty(Mono.error(new UserNotFoundException("No User Found with Username: " + username)))
				.flatMap(user -> {
					user.setPassword(updatedPassword.getPassword());
					return userRepo.save(user);
				});
	}

	@Override
	public Mono<User> saveUser(@Valid User user) {
		return userRepo.save(user);
	}

	@Override
	public Flux<User> getUserList() {
		Flux<User> users = userRepo.findAll();
		return users
				.switchIfEmpty
				(Mono.error
						(new UserNotFoundException
								("No User Found.")));
	}
}
