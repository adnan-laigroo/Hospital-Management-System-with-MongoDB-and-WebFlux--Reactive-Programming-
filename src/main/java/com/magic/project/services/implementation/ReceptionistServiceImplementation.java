package com.magic.project.services.implementation;

import com.magic.project.exceptionHandler.ReceptionistNotFoundException;
import com.magic.project.models.Receptionist;
import com.magic.project.repository.ReceptionistRepository;
import com.magic.project.services.ReceptionistService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class ReceptionistServiceImplementation implements ReceptionistService {
	@Autowired
	ReceptionistRepository recepRepo;

	@Override
	public Mono<Receptionist> saveReceptionist(@Valid Receptionist receptionist) {
		return recepRepo.save(receptionist);
	}

	@Override
	public Mono<Receptionist> deleteReceptionist(@Valid String email) {
		return recepRepo.findById(email)
				.switchIfEmpty(Mono.error(new ReceptionistNotFoundException("No Receptionist with ID " + email)))
				.flatMap(receptionist -> recepRepo.deleteById(email).thenReturn(receptionist));
				
	}

	@Override
	public Mono<Receptionist> updateReceptionist(Receptionist updatedReceptionist, @Valid String email) {
		return recepRepo.findById(email)
				.switchIfEmpty(Mono.error(new ReceptionistNotFoundException("No Receptionist with ID " + email)))
				.flatMap(receptionist -> {
					updatedReceptionist.setEmail(email);
					return recepRepo.save(updatedReceptionist);});
	}

	@Override
	public Flux<Receptionist> getReceptionistList() {
		Flux<Receptionist> receptionistsFlux = recepRepo.findAll();
		return receptionistsFlux
				.switchIfEmpty(Mono.error(new ReceptionistNotFoundException("No Receptionist Found.")));
	}

	@Override
	public Mono<Receptionist> getReceptionist(String email) {
		return recepRepo.findById(email)
				.switchIfEmpty(Mono.error(new ReceptionistNotFoundException("No Receptionist with ID " + email)));
	}


}