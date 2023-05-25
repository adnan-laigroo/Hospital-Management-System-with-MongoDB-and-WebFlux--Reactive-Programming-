package com.magic.project.services;

import com.magic.project.models.Appointment;

import javax.validation.Valid;
import java.util.List;

public interface AppointmentService {

	void saveAppointment(@Valid Appointment appointment);

	Appointment deleteAppointment(@Valid String appId);

	Appointment updateAppointment(Appointment updatedAppointment, @Valid String appId);
	
	List<Appointment> getAppointmentList();

	Appointment updateAppointmentStatus(Appointment updatedAppointment, @Valid String appId);

}