package com.goodcompany.salamport.repository;

import com.goodcompany.salamport.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(String email);

    ArrayList<User> findAllById(long id);
}
