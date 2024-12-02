package com.rental.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rental.api.model.User;
import com.rental.api.service.JWTService;
import com.rental.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    private JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String schemaExampleLogin = "{ \"email\": \"test@test.com\", \"password\": \"Password123\" }";
    private static final String schemaExampleRegister = "{ \"name\": \"Test\", \"email\": \"test@test.com\", \"password\": \"Password123\" }";

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "User account connection")
    @PostMapping("/auth/login")
    public String login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExampleLogin))) 
            @RequestBody User user) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
            String token = jwtService.generateToken(authentication);

            return token;

        } catch (BadCredentialsException e) { // email not found
            return "Wrong email or password";
        } catch (Exception e) { // error password
            return "Wrong email or password";
        }
    }

    @Operation(summary = "Create a new user account")
    @PostMapping("/auth/register")
    public ResponseEntity<User> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExampleRegister))) 
            @RequestBody User user)
            throws Exception {

        User registeredUser = userService.saveUser(user);

        return ResponseEntity.ok(registeredUser);
    }

    @Operation(summary = "Get current user account informations")
    @GetMapping("/auth/me")
    public User getCurrentUser(Principal principal) {
        return userService.getUserByMail(principal.getName());
    }
}