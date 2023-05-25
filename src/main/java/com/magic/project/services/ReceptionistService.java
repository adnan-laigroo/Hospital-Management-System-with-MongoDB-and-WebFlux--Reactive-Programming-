package com.magic.project.services;

import com.magic.project.models.Receptionist;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface ReceptionistService {

	Mono<Receptionist> saveReceptionist(@Valid Receptionist receptionist);

	Mono<Receptionist> deleteReceptionist(@Valid String email);

	Mono<Receptionist> updateReceptionist(Receptionist updatedReceptionist, @Valid String email);

	Flux<Receptionist> getReceptionistList();

}