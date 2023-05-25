package com.magic.project.services;

import com.magic.project.models.Doctor;

import javax.validation.Valid;
import java.util.List;

public interface DoctorService {

	void saveDoctor(@Valid Doctor doctor);

	Doctor deleteDoctor(@Valid String email);

	Doctor updateDoctor(Doctor updatedDoctor, @Valid String email);

	List<Doctor> getDoctorList();

}