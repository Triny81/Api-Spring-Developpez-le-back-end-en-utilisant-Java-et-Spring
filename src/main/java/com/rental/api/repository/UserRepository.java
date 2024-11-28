package com.rental.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rental.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findByEmail(String email);
}
