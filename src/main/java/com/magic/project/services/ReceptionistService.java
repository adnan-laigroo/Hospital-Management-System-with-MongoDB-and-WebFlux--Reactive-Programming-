package com.magic.project.services;

import com.magic.project.models.Receptionist;

import javax.validation.Valid;
import java.util.List;

public interface ReceptionistService {

	void saveReceptionist(@Valid Receptionist receptionist);

	Receptionist deleteReceptionist(@Valid String email);

	Receptionist updateReceptionist(Receptionist updatedReceptionist, @Valid String email);

	List<Receptionist> getReceptionistList();

}