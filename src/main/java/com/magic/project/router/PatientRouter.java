package com.magic.project.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.handler.PatientHandler;

@Configuration
public class PatientRouter {

	@Bean
	public RouterFunction<ServerResponse> patientRoutes(PatientHandler patientHandler) {
		return RouterFunctions.route().POST("/hospital/patient/add", patientHandler::addPatient)
				.DELETE("/hospital/patient/delete/{patId}", patientHandler::deletePatient)
				.PUT("/hospital/patient/update/{patId}", patientHandler::updatePatient)
				.GET("/hospital/patient/list", patientHandler::getAllPatients).build();
	}
}
