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
import java.time.LocalDateTime;
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
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalTime appointmentTime = LocalTime.parse(appointment.getAppointmentTime(), formatter);
		if (appointmentTime.isBefore(currentDateTime.toLocalTime())) {
			return Mono.error(new AppointmentNotConfirmedException("Appointment for time: "
					+ appointment.getAppointmentTime() + " can't be booked before: " + currentDateTime.toLocalTime()));
		}

		Mono<Patient> patientMono = patRepo.findById(appointment.getPatId()).switchIfEmpty(
				Mono.error(new PatientNotFoundException("No patient found of patient Id: " + appointment.getPatId())));

		Mono<List<Doctor>> doctorListMono = patientMono.flatMap(patient -> docRepo
				.findBySpeciality(symptomSpecialityMap.get(patient.getSymptom()))
				.switchIfEmpty(
						Mono.error(new DoctorNotFoundException("No doctor found for symptom " + patient.getSymptom())))
				.collectList());

		Mono<String> doctorWithLeastPendingAppointmentsMono = doctorListMono
				.flatMap(doctorList -> Flux.fromIterable(doctorList)
						.flatMap(doctor -> appRepo
								.findByDocIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatus(doctor.getEmail(),
										appointment.getAppointmentDate(), appointmentTime, "Pending")
								.count().map(count -> new DoctorAppointmentCount(doctor, count)))
						.reduce((minCountDoctor, currentDoctor) -> currentDoctor.getCount() < minCountDoctor.getCount()
								? currentDoctor
								: minCountDoctor)
						.map(DoctorAppointmentCount::getDoctor).map(Doctor::getEmail));

		Mono<Appointment> generatedAppointmentWithCustomIdMono = appRepo.findAll().count().flatMap(count -> {
			int customId = (int) (count != 0 ? count + 1 : 1);
			appointment.generateCustomId(customId);
			return Mono.just(appointment);
		});

		return doctorWithLeastPendingAppointmentsMono.flatMap(selectedDoctorEmail -> {
			return generatedAppointmentWithCustomIdMono.flatMap(generatedAppointment -> {
				generatedAppointment.setAppointmentDate(currentDateTime.toLocalDate());
				generatedAppointment.setAppointmentTime(appointmentTime.format(formatter));
				generatedAppointment.setAppointmentStatus("Pending");
				generatedAppointment.setDocId(selectedDoctorEmail);
				return appRepo.save(generatedAppointment);
			});
		});
	}

	private static class DoctorAppointmentCount {
		private final Doctor doctor;
		private final long count;

		public DoctorAppointmentCount(Doctor doctor, long count) {
			this.doctor = doctor;
			this.count = count;
		}

		public Doctor getDoctor() {
			return doctor;
		}

		public long getCount() {
			return count;
		}

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
