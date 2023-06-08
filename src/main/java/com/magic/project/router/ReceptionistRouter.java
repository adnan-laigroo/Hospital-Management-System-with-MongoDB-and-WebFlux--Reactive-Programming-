package com.magic.project.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.magic.project.handler.ReceptionistHandler;

@Configuration
public class ReceptionistRouter {

	@Bean
	public RouterFunction<ServerResponse> receptionistRoutes(ReceptionistHandler receptionistHandler) {
		return RouterFunctions.route().POST("/hospital/receptionist/add", receptionistHandler::addReceptionist)
				.DELETE("/hospital/receptionist/delete/{email}", receptionistHandler::deleteReceptionist)
				.PUT("/hospital/receptionist/update/{email}", receptionistHandler::updateReceptionist)
				.GET("/hospital/receptionist/get/{email}", receptionistHandler::getReceptionistById)
				.GET("/hospital/receptionist/list", receptionistHandler::getAllReceptionists).build();
	}
}
