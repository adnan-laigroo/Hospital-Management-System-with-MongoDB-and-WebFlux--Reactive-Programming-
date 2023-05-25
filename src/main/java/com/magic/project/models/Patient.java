package com.magic.project.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document("patients")
public class Patient {

	@Id
	private String patId;

	@NotBlank(message = "First name is required")
	@Pattern(regexp = "^[A-Za-z]+$", message = "Only alphabets are allowed for first name")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Pattern(regexp = "^[A-Za-z]+$", message = "Only alphabets are allowed for last name")
	private String lastName;

	private AddressD address;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number should contain exactly 10 digits")
	private String phoneNo;

	@NotNull(message = "Symptom is required")
	@Pattern(regexp = "^(Arthritis|Backpain|Tissue injuries|Dysmenorrhea|Skin infection|Skin burn|Ear pain)$", message = "Invalid symptom")
	private String symptom;

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public AddressD getAddress() {
		return address;
	}

	public void setAddress(AddressD address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getSymptom() {
		return symptom;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

}
