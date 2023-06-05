package com.magic.project.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.magic.project.handler.AppointmentHandler;

@Configuration
public class AppointmentRouter {

	@Bean
	public RouterFunction<ServerResponse> appointmentRoutes(AppointmentHandler appointmentHandler) {
		return RouterFunctions.route().POST("/hospital/appointment/add", appointmentHandler::addAppointment)
				.DELETE("/hospital/appointment/delete/{appId}", appointmentHandler::deleteAppointment)
				.PUT("/hospital/appointment/update/{appId}", appointmentHandler::updateAppointment)
				.GET("/hospital/appointment/list", appointmentHandler::getAllAppointments)
				.GET("/hospital/appointment/get/{appId}", appointmentHandler::getAppointmentById)
				.PATCH("/hospital/appointment/update/status/{appId}", appointmentHandler::updateAppointmentStatus)
				.build();
	}
}
