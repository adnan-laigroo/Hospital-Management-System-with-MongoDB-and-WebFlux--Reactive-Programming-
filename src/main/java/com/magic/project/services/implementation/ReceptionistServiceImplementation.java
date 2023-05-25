package com.magic.project.services.implementation;

import com.magic.project.handler.ReceptionistNotFoundException;
import com.magic.project.models.Receptionist;
import com.magic.project.repository.ReceptionistRepository;
import com.magic.project.services.ReceptionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class ReceptionistServiceImplementation implements ReceptionistService {
	@Autowired
	ReceptionistRepository recepRepo;

	@Override
	public void saveReceptionist(@Valid Receptionist receptionist) {
		recepRepo.save(receptionist);

	}

	@Override
	public Receptionist deleteReceptionist(@Valid String email) {
		Receptionist receptionist = recepRepo.findById(email).orElse(null);
		if (receptionist == null) {
			throw new ReceptionistNotFoundException("No Receptionist with ID " + email);
		}
		recepRepo.deleteById(email);
		return receptionist;
	}

	@Override
	public Receptionist updateReceptionist(Receptionist updatedReceptionist, @Valid String email) {
		Receptionist receptionist = recepRepo.findById(email).orElse(null);
		if (receptionist == null) {
			throw new ReceptionistNotFoundException("No Receptionist with ID " + email);
		}
		updatedReceptionist.setEmail(email);
		recepRepo.save(updatedReceptionist);
		return updatedReceptionist;
	}

	@Override
	public List<Receptionist> getReceptionistList() {
		List<Receptionist> receptionists = recepRepo.findAll();
		if (receptionists == null) {
			throw new ReceptionistNotFoundException("No Receptionist Found.");
		}
		return receptionists;
	}

}