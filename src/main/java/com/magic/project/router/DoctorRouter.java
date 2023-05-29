package com.magic.project.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.handler.DoctorHandler;

@Configuration
public class DoctorRouter {

	@Bean
	public RouterFunction<ServerResponse> doctorRoutes(DoctorHandler doctorHandler) {
		return RouterFunctions.route().POST("/hospital/doctor/add", doctorHandler::addDoctor)
				.DELETE("/hospital/doctor/delete/{email}", doctorHandler::deleteDoctor)
				.PUT("/hospital/doctor/update/{email}", doctorHandler::updateDoctor)
				.GET("/hospital/doctor/list", doctorHandler::getAllDoctors).build();
	}
}
