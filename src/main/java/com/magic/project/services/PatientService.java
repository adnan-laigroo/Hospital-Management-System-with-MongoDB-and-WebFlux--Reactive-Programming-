package com.magic.project.services;

import com.magic.project.models.Patient;

import javax.validation.Valid;
import java.util.List;

public interface PatientService {

	void savePatient(@Valid Patient patient);

	Patient deletePatient(@Valid String patId);

	Patient updatePatient(Patient updatedPatient, @Valid String patId);

	List<Patient> getPatientList();

}