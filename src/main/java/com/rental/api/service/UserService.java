package com.rental.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.api.configuration.SpringSecurityConfig;
import com.rental.api.model.User;
import com.rental.api.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {
    @Autowired
    private SpringSecurityConfig ssc;

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    public User getUserByMail(final String email) {
        return userRepository.findByEmail(email);
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) throws Exception {

        if (user.getEmail() != null) {
            User userFound = userRepository.findByEmail(user.getEmail());
            
            if (userFound != null && userFound.getEmail() != null && (user.getId() == null || !user.getId().equals(userFound.getId()))) {
                throw new Exception("Email already exists");
            }
        }

        user.setPassword(ssc.passwordEncoder().encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
