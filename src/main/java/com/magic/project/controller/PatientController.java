package com.magic.project.controller;

import com.magic.project.models.Patient;
import com.magic.project.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hospital/patient")
public class PatientController {
	@Autowired
	PatientService patServ;

	// Add a Patient
	@PostMapping("/add")
	public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
		patServ.savePatient(patient);
		return ResponseEntity.status(HttpStatus.OK).body(patient);
	}

	// delete a Patient
	@DeleteMapping("/delete/{patId}")
	public ResponseEntity<Patient> deletePatient(@Valid @PathVariable String patId) {
		Patient patient = patServ.deletePatient(patId);
		return ResponseEntity.status(HttpStatus.OK).body(patient);
	}

	// update a Patient by ID and Put request
	@PutMapping("/update/{patId}")
	public ResponseEntity<Patient> updatePatient(@Valid @PathVariable String patId,
			@RequestBody Patient updatedPatient) {
		Patient patient = patServ.updatePatient(updatedPatient, patId);
		return ResponseEntity.status(HttpStatus.OK).body(patient);
	}

	// get list of all Patients
	@GetMapping("/list")
	public ResponseEntity<List<Patient>> getAllPatient() {
		List<Patient> patients = patServ.getPatientList();
		return ResponseEntity.status(HttpStatus.OK).body(patients);
	}
}
