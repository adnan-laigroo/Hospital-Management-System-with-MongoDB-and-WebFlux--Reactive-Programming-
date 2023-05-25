package com.magic.project.services.implementation;

import com.magic.project.handler.PatientNotFoundException;
import com.magic.project.models.Patient;
import com.magic.project.repository.PatientRepository;
import com.magic.project.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class PatientServiceImplementation implements PatientService {
	@Autowired
	PatientRepository patRepo;

	@Override
	public void savePatient(@Valid Patient Patient) {
		patRepo.save(Patient);

	}

	@Override
	public Patient deletePatient(@Valid String patId) {
		Patient Patient = patRepo.findById(patId).orElse(null);
		if (Patient == null) {
			throw new PatientNotFoundException("No Patient with ID " + patId);
		}
		patRepo.deleteById(patId);
		return Patient;
	}

	@Override
	public Patient updatePatient(Patient updatedPatient, @Valid String patId) {
		Patient Patient = patRepo.findById(patId).orElse(null);
		if (Patient == null) {
			throw new PatientNotFoundException("No Patient with ID " + patId);
		}
		updatedPatient.setPatId(patId);
		patRepo.save(updatedPatient);
		return updatedPatient;
	}

	@Override
	public List<Patient> getPatientList() {
		List<Patient> patients = patRepo.findAll();
		if (patients.isEmpty()) {
			throw new PatientNotFoundException("No Patient Found.");
		}
		return patients;
	}
}