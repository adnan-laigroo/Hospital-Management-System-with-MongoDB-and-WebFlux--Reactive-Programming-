package com.magic.project.repository;

import com.magic.project.models.Receptionist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends MongoRepository<Receptionist, String> {

}
