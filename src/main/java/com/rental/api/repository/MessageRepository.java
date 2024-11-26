package com.rental.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rental.api.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    
}
