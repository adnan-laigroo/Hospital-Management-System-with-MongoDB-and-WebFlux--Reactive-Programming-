package com.magic.project.repository;

import com.magic.project.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDRepository extends ReactiveMongoRepository<User, String> {

}