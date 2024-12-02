package com.rental.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rental.api.model.User;
import com.rental.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private static final String schemaExample = "{ \"name\": \"Test\", \"email\": \"test@test.com\", \"password\": \"Password123\" }";

    @Operation(summary = "Create a new user")
    @PostMapping("/user")
    public User createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample)))
            @RequestBody User user) throws Exception {
        return userService.saveUser(user);
    }

    @Operation(summary = "Get one user by id")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    @Operation(summary = "Get one user by email")
    @GetMapping("/user/email/{email}")
    public User getUser(@PathVariable("email") final String email) {
        User user = userService.getUserByMail(email);

        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Update an existing user")
    @PutMapping("/user/{id}")
    public User updateUser(          
            @PathVariable("id") final Long id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample)))
            @RequestBody User user) throws Exception {
        Optional<User> u = userService.getUser(id);

        if (u.isPresent()) {
            User currentUser = u.get();

            String email = user.getEmail();
            if (email != null) {
                currentUser.setEmail(email);
            }

            String name = user.getName();
            if (name != null) {
                currentUser.setName(name);
            }

            String password = user.getPassword();
            if (password != null) {
                currentUser.setPassword(password);
            }

            userService.saveUser(currentUser);

            return currentUser;
        } else {
            return null;
        }
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUser(id);
    }
}
