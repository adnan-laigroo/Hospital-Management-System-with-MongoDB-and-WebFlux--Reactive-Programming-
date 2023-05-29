package com.magic.project.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.magic.project.handler.UserHandler;

@Configuration
public class UserRouter {

	@Bean
	public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
		return RouterFunctions.route()
				.PATCH("/hospital/user/update/password/{username}", userHandler::updateUserPassword)
				.GET("/hospital/user/list", userHandler::getAllUsers).build();
	}
}
