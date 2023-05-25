package com.magic.project.services.implementation;

import com.magic.project.handler.DoctorNotFoundException;
import com.magic.project.models.Doctor;
import com.magic.project.repository.DoctorRepository;
import com.magic.project.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class DoctorServiceImplementation implements DoctorService {
	@Autowired
	DoctorRepository docRepo;

	@Override
	public void saveDoctor(@Valid Doctor doctor) {
		docRepo.save(doctor);
	}

	@Override
	public Doctor deleteDoctor(@Valid String email) {
		Doctor doctor = docRepo.findById(email).orElse(null);
		if (doctor==null){
			throw  new DoctorNotFoundException("No doctor found with ID "+email);
		}
		docRepo.deleteById(email);
		return doctor;
	}

	@Override
	public Doctor updateDoctor(Doctor updatedDoctor, @Valid String email) {
		Doctor doctor = docRepo.findById(email).orElse(null);
		if (doctor==null){
			throw  new DoctorNotFoundException("No doctor found with ID "+email);
		}
		updatedDoctor.setEmail(email);
		docRepo.save(updatedDoctor);
		return updatedDoctor;
	}

	@Override
	public List<Doctor> getDoctorList() {
		List<Doctor> doctors = docRepo.findAll();
		if (doctors.isEmpty()){
			throw  new DoctorNotFoundException("No doctor found.");
		}
		return doctors;
	}
}