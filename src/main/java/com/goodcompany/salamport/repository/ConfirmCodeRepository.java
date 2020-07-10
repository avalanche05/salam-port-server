package com.goodcompany.salamport.repository;

import com.goodcompany.salamport.models.ConfirmCode;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmCodeRepository extends CrudRepository<ConfirmCode,Long> {
    boolean existsByCode(int code);
    ConfirmCode findByCone(int code);
}
