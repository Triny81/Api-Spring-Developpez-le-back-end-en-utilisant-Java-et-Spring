package com.rental.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rental.api.model.User;
import com.rental.api.service.JWTService;
import com.rental.api.service.UserService;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    private JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody User user)  {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
            String token = jwtService.generateToken(authentication);

            return token;

        }catch (BadCredentialsException e){ // email not found
            return "Wrong email or password";
        }catch (Exception e){ // error password
            return "Wrong email or password";
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody User user) throws Exception {
        User registeredUser = userService.saveUser(user);

        return ResponseEntity.ok(registeredUser);
    }
}