package com.magic.project.services.implementation;

import com.magic.project.handler.UserNotFoundException;
import com.magic.project.models.Password;
import com.magic.project.models.User;
import com.magic.project.repository.UserDRepository;
import com.magic.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	UserDRepository userRepo;

	@Override
	public User updateUserPassword(@Valid Password updatedPassword, String username) {
		User user = userRepo.findById(username).orElse(null);
		if (user == null) {
			throw new UserNotFoundException("No User Found with Username: " + username);
		}
		userRepo.save(user);
		return user;
	}

	@Override
	public void saveUser(@Valid User user) {
		userRepo.save(user);
	}

	@Override
	public List<User> getUserList() {
		List<User> users = userRepo.findAll();
		if (users.isEmpty()) {
			throw new UserNotFoundException("No User Found.");
		}
		return users;
	}
}
