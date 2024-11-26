package com.rental.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rental.api.model.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {
    
}
