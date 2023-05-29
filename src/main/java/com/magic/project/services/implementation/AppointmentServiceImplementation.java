package com.magic.project.services.implementation;

import com.magic.project.exceptionHandler.AppointmentNotConfirmedException;
import com.magic.project.exceptionHandler.DoctorNotFoundException;
import com.magic.project.exceptionHandler.PatientNotFoundException;
import com.magic.project.models.Appointment;
import com.magic.project.models.Doctor;
import com.magic.project.models.Patient;
import com.magic.project.repository.AppointmentRepository;
import com.magic.project.repository.DoctorRepository;
import com.magic.project.repository.PatientRepository;
import com.magic.project.services.AppointmentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImplementation implements AppointmentService {

	@Autowired
	AppointmentRepository appRepo;

	@Autowired
	PatientRepository patRepo;

	@Autowired
	DoctorRepository docRepo;

	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	private static Map<String, String> symptomSpecialityMap = new HashMap<>();
	static {
		symptomSpecialityMap.put("Arthritis", "Orthopedic");
		symptomSpecialityMap.put("Backpain", "Orthopedic");
		symptomSpecialityMap.put("Tissue injuries", "Orthopedic");
		symptomSpecialityMap.put("Dysmenorrhea", "Gynecology");
		symptomSpecialityMap.put("Skin infection", "Dermatology");
		symptomSpecialityMap.put("Skin burn", "Dermatology");
		symptomSpecialityMap.put("Ear pain", "ENT");
	}

	@Override
	public Mono<Appointment> saveAppointment(@Valid Appointment appointment) {
		appointment.setAppointmentDate(LocalDate.now());
		appointment.setAppointmentTime(LocalTime.parse(appointment.getAppointmentTime(), formatter).format(formatter));
		appointment.setAppointmentStatus("Pending");
		if (LocalTime.parse(appointment.getAppointmentTime(), formatter).isBefore(LocalTime.now())) {
			throw new AppointmentNotConfirmedException("Appointment for time: " + appointment.getAppointmentTime()
					+ " can't be booked before: " + LocalTime.now());
		}

		Mono<Patient> patientMono = patRepo.findById(appointment.getPatId()).switchIfEmpty(
				Mono.error(new PatientNotFoundException("No patient found of patient Id: " + appointment.getPatId())));

		Mono<List<Doctor>> doctorListMono = patientMono.flatMap(patient -> {
			return docRepo.findBySpeciality(symptomSpecialityMap.get(patient.getSymptom()))
					.switchIfEmpty(Mono
							.error(new DoctorNotFoundException("No doctor found for symptom " + patient.getSymptom())))
					.collectList();
		});

		Mono<Appointment> doctorWithLeastPendingAppointmentsMono = doctorListMono.flatMap(doctorList -> {
			String doctorWithLeastPendingAppointments = getDoctorWithLeastPendingAppointments(doctorList);
			return appRepo.findByDocId(doctorWithLeastPendingAppointments).collectList().flatMap(doctorAppointments -> {
				for (Appointment existingAppointment : doctorAppointments) {
					if (existingAppointment.getAppointmentDate().equals(appointment.getAppointmentDate())
							&& existingAppointment.getAppointmentTime().equals(appointment.getAppointmentTime())) {
						// Appointment time conflict found, remove the doctor and get the next available
						// doctor
						String oldDoctor = doctorWithLeastPendingAppointments;
						doctorList.removeIf(doctor -> doctor.getEmail().equals(oldDoctor));
						doctorWithLeastPendingAppointments = getDoctorWithLeastPendingAppointments(doctorList);
						break;
					}
				}
				appointment.setDocId(doctorWithLeastPendingAppointments);
				return Mono.just(appointment);
			});
		});

		return doctorWithLeastPendingAppointmentsMono.flatMap(updatedAppointment -> appRepo.save(updatedAppointment));
	}

	private String getDoctorWithLeastPendingAppointments(List<Doctor> doctorList) {
		String doctorWithLeastPendingAppointments = doctorList.get(0).getEmail();
		int minPendingAppointments = countPendingAppointments(doctorWithLeastPendingAppointments);
		for (Doctor doctor : doctorList) {
			int pendingAppointments = countPendingAppointments(doctor.getEmail());
			if (pendingAppointments < minPendingAppointments) {
				minPendingAppointments = pendingAppointments;
				doctorWithLeastPendingAppointments = doctor.getEmail();
			}
		}
		return doctorWithLeastPendingAppointments;
	}

	private int countPendingAppointments(String email) {
		int count = 0;
		Flux<Appointment> appointments = appRepo.findByDocId(email);
		appointments.filter(appointment -> appointment.getAppointmentStatus().equals("Pending"));
		count = appointments.toStream().filter(appointment -> appointment.getAppointmentStatus().equals("Pending"))
				.collect(Collectors.toList()).size();
		return count;
	}

	@Override
	public Mono<Appointment> deleteAppointment(@Valid String apId) {
		Mono<Appointment> appointment = appRepo.findById(apId)
				.switchIfEmpty(Mono.error(new AppointmentNotConfirmedException("No Appointment with ID ")));
		appRepo.deleteById(apId);
		return appointment;
	}

	@Override
	public Mono<Appointment> updateAppointment(Appointment updatedAppointment, @Valid String apId) {
		return appRepo.findById(apId)
				.switchIfEmpty(Mono.error(new AppointmentNotConfirmedException("No Appointment with ID " + appRepo)))
				.flatMap(appointment -> {
					updatedAppointment.setApId(apId);
					return appRepo.save(updatedAppointment);
				});
	}

	@Override
	public Flux<Appointment> getAppointmentList() {
		Flux<Appointment> appointmentsFlux = appRepo.findAll()
				.switchIfEmpty(Mono.error(new AppointmentNotConfirmedException("No Appointment with ID ")));
		return appointmentsFlux;
	}

	@Override
	public Mono<Appointment> updateAppointmentStatus(Appointment updatedAppointment, @Valid String apId) {
		Mono<Appointment> appointmentMono = appRepo.findById(apId)
				.switchIfEmpty(Mono.error(new AppointmentNotConfirmedException("No Appointment with ID ")))
				.flatMap(appointment -> {
					appointment.setAppointmentStatus(updatedAppointment.getAppointmentStatus());
					return appRepo.save(appointment);
				});
		return appointmentMono;
	}
}
