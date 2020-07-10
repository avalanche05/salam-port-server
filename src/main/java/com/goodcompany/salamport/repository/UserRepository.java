package com.goodcompany.salamport.repository;

import com.goodcompany.salamport.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(String email);
}
