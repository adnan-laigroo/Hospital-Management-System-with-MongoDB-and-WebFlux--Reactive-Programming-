package com.magic.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.magic.project.models.Appointment;
import com.magic.project.services.AppointmentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hospital/appointment")
public class AppointmentController {
	@Autowired
	AppointmentService appServ;

	// Add a Appointment
	@PostMapping("/add")
	public ResponseEntity<Appointment> addAppointment(@Valid @RequestBody Appointment appointment) {
		appServ.saveAppointment(appointment);
		return ResponseEntity.status(HttpStatus.OK).body(appointment);
	}

	// delete a Appointment
	@DeleteMapping("/delete/{appId}")
	public ResponseEntity<Appointment> deleteAppointment(@Valid @PathVariable String appId) {
		Appointment appointment = appServ.deleteAppointment(appId);
		return ResponseEntity.status(HttpStatus.OK).body(appointment);
	}

	// update a Appointment by ID and Put request
	@PutMapping("/update/{appId}")
	public ResponseEntity<Appointment> updateAppointment(@Valid @PathVariable String appId,
			@RequestBody Appointment updatedAppointment) {
		Appointment appointment = appServ.updateAppointment(updatedAppointment, appId);
		return ResponseEntity.status(HttpStatus.OK).body(appointment);
	}

	// get list of all Appointments
	@GetMapping("/list")
	public ResponseEntity<List<Appointment>> getAllAppointment() {
		List<Appointment> appointments = appServ.getAppointmentList();
		return ResponseEntity.status(HttpStatus.OK).body(appointments);
	}

	// update a Appointment Status by ID and Patch request
	@PatchMapping("/update/status/{appId}")
	public ResponseEntity<Appointment> updateAppointmentStaus(@Valid @PathVariable String appId,
			@RequestBody Appointment updatedAppointment) {
		Appointment appointment = appServ.updateAppointmentStatus(updatedAppointment, appId);
		return ResponseEntity.status(HttpStatus.OK).body(appointment);
	}

}
