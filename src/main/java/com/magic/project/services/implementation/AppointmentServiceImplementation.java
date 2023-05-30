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
	public Mono<Appointment> saveOtherAppointment(@Valid Appointment appointment) {
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

		return doctorListMono.flatMap(doctorList -> {
			if (doctorList.isEmpty()) {
				// No doctors available for the appointment
				return Mono.error(new AppointmentNotConfirmedException("No available doctors for the appointment"));
			} else {
				// Filter out doctors who have a clash with the appointment time
				Flux<Doctor> availableDoctors = Flux.fromIterable(doctorList).flatMap(
						doctor -> isDateTimeClash(doctor.getEmail(), appointment.getAppointmentDate(), appointmentTime)
								.filter(isClash -> !isClash).map(isClash -> doctor));
				return availableDoctors.collectList().flatMap(availableDoctorList -> {
					if (availableDoctorList.isEmpty()) {
						// No available doctors with no clashes, throw an exception
						return Mono.error(
								new AppointmentNotConfirmedException("No available doctors for the appointment"));
					} else {
						// Select a random doctor from the available list
						Doctor selectedDoctor = availableDoctorList
								.get(new Random().nextInt(availableDoctorList.size()));
						appointment.setDocId(selectedDoctor.getEmail());

						// Generate custom ID
						return generateCustomId().flatMap(customId -> {
							appointment.generateCustomId(customId);

							// Check for clashes with the selected doctor's appointments
							return isDateTimeClash(selectedDoctor.getEmail(), appointment.getAppointmentDate(),
									appointmentTime).flatMap(hasClashes -> {
										if (hasClashes) {
											// Select the next available doctor
											return selectNextAvailableDoctor(doctorListMono, appointment);
										} else {
											// No clash, save the appointment with the selected doctor
											appointment.setAppointmentDate(currentDateTime.toLocalDate());
											appointment.setAppointmentTime(appointmentTime.format(formatter));
											appointment.setAppointmentStatus("Pending");
											return appRepo.save(appointment);
										}
									});
						});
					}
				});
			}
		});
	}

	@Override
	public Mono<Appointment> saveFirstAppointment(@Valid Appointment appointment) {
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

		Mono<Appointment> firstAppointmentMono = doctorListMono.flatMap(doctorList -> {
			if (doctorList.isEmpty()) {
				// No doctors available for the appointment
				return Mono.error(new AppointmentNotConfirmedException("No available doctors for the appointment"));
			}
			Doctor selectedDoctor = doctorList.get(new Random().nextInt(doctorList.size()));
			appointment.setDocId(selectedDoctor.getEmail());

			// Generate custom ID
			return generateCustomId().flatMap(customId -> {
				appointment.generateCustomId(customId);

				// Check for clashes with the selected doctor's appointments
				return Mono.just(appointment).flatMap(firstAppointment -> {
					// No clash, save the appointment with the selected doctor
					appointment.setAppointmentDate(currentDateTime.toLocalDate());
					appointment.setAppointmentTime(appointmentTime.format(formatter));
					appointment.setAppointmentStatus("Pending");
					return appRepo.save(appointment);
				});
			});
		});
		return firstAppointmentMono;

	}

	private Mono<Boolean> isDateTimeClash(String doctorEmail, LocalDate appointmentDate, LocalTime appointmentTime) {
		return appRepo
				.findByDocIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatus(doctorEmail, appointmentDate,
						LocalTime.parse(appointmentTime.format(formatter), formatter), "Pending")
				.hasElements().map(hasElements -> !hasElements);
	}

	private Mono<Appointment> selectNextAvailableDoctor(Mono<List<Doctor>> doctorListMono, Appointment appointment) {
		return doctorListMono.flatMap(doctorList -> {
			// Filter out doctors who have a clash with the appointment time
			Flux<Doctor> availableDoctors = Flux.fromIterable(doctorList)
					.flatMap(doctor -> isDateTimeClash(doctor.getEmail(), appointment.getAppointmentDate(),
							LocalTime.parse(appointment.getAppointmentTime(), formatter)).filter(isClash -> !isClash)
							.map(isClash -> doctor));
			return availableDoctors.collectList().flatMap(availableDoctorList -> {
				if (availableDoctorList.isEmpty()) {
					// No available doctors with no clashes, throw an exception
					return Mono.error(new AppointmentNotConfirmedException("No available doctors for the appointment"));
				} else {
					// Select a random doctor from the available list
					Doctor selectedDoctor = availableDoctorList.get(new Random().nextInt(availableDoctorList.size()));
					appointment.setDocId(selectedDoctor.getEmail());

					// Check for clashes with the selected doctor's appointments
					return isDateTimeClash(selectedDoctor.getEmail(), appointment.getAppointmentDate(),
							LocalTime.parse(appointment.getAppointmentTime(), formatter)).flatMap(hasClashes -> {
								if (hasClashes) {
									// Select the next available doctor
									return selectNextAvailableDoctor(doctorListMono, appointment);
								} else {
									// No clash, save the appointment with the selected doctor
									LocalDateTime currentDateTime = LocalDateTime.now();
									LocalTime appointmentTime = LocalTime.parse(appointment.getAppointmentTime(),
											formatter);
									appointment.setAppointmentDate(currentDateTime.toLocalDate());
									appointment.setAppointmentTime(appointmentTime.format(formatter));
									appointment.setAppointmentStatus("Pending");
									return appRepo.save(appointment);
								}
							});
				}
			});
		});
	}

	private Mono<Integer> generateCustomId() {
		return appRepo.findAll().count().map(count -> count.intValue() != 0 ? count.intValue() + 1 : 1);
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

	@Override
	public Mono<Appointment> saveAppointment(@Valid Appointment appointment) {
		return appRepo.findAll().count().flatMap(count -> {
			if (count == 0) {
				return saveFirstAppointment(appointment);
			} else {
				return saveOtherAppointment(appointment);
			}
		});
	}
}
