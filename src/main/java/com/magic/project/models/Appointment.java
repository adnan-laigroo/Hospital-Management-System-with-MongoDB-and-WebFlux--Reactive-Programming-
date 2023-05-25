package com.magic.project.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document("appointments")
public class Appointment {
	@Id
	private String apId;

	@NotBlank(message = "Patient ID cannot be blank")
	private String patId;

	@NotBlank(message = "Blood group cannot be blank")
	@Pattern(regexp = "^(O|A|B|AB)[+-]$", message = "Invalid blood group")
	private String bloodGroup;

	private String docId;

	private LocalDate appointmentDate;

	@NotBlank(message = "Appointment time cannot be blank")
	private String appointmentTime;

	private String appointmentStatus;

	public String getApId() {
		return apId;
	}

	public void setApId(String apId) {
		this.apId = apId;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

}
