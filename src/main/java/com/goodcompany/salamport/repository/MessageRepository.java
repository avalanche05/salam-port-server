package com.goodcompany.salamport.repository;

import com.goodcompany.salamport.models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface MessageRepository extends CrudRepository<Message,Long> {
    ArrayList<Message> findAllByTo(long to);
    ArrayList<Message> findAllByFromAndTo(long from, long to);
}
