package com.magic.project.exceptionHandler;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message) {
		super(message);

	}

}
