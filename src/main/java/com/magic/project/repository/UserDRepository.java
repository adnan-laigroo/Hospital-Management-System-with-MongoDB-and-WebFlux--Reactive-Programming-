package com.magic.project.repository;

import com.magic.project.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDRepository extends MongoRepository<User, String> {

}