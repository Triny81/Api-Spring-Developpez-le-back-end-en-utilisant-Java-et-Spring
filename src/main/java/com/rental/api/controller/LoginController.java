package com.rental.api.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rental.api.dto.UserDTO;
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

    @Autowired
    private ModelMapper modelMapper;

    private JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String schemaExampleLogin = "{ \"email\": \"test@test.com\", \"password\": \"Password123\" }";
    private static final String schemaExampleRegister = "{ \"name\": \"Test\", \"email\": \"test@test.com\", \"password\": \"Password123\" }";

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "User account connection")
    @PostMapping("/api/auth/login")
    public String login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "No authentication required.<br> Returns a JWT token when the login is correct.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExampleLogin))) 
            @RequestBody User user) {

        return getTokenJSON(user.getEmail(), user.getPassword());      
    }

    @Operation(summary = "Create a new user account")
    @PostMapping("/api/auth/register")
    public String register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "No authentication required.<br> Returns a JWT token when the account is created.<br> Returns an error if the email already exists.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExampleRegister))) 
            @RequestBody User user)
            throws Exception {
        
        String uncryptedPassoword = user.getPassword();
        User registeredUser = userService.saveUser(user);

        if (registeredUser != null) {
            return getTokenJSON(user.getEmail(), uncryptedPassoword);
        } else {
            return "Registration failed";
        }
    }

    @Operation(summary = "Get current user account informations")
    @GetMapping("/api/auth/me")
    public UserDTO getCurrentUser(Principal principal) {
        if (principal != null) {
            User user = userService.getUserByMail(principal.getName());
            return convertToDto(user);
        } else {
            return null;
        }
    }

    private UserDTO convertToDto(User user) {
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return userDTO;
	}

    private String getTokenJSON(String email, String encryptedPassword) { // return token in a JSON

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, encryptedPassword));
            String token = jwtService.generateToken(authentication);
    
            return "{ \"token\": \""+ token +"\" }";

        } catch (BadCredentialsException e) { // email not found
            return "Wrong email or password";
        } catch (Exception e) { // error password
            return "Wrong email or password";
        }
    }
}