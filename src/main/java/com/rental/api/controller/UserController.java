package com.rental.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rental.api.dto.UserDTO;
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

    @Autowired
    private ModelMapper modelMapper;

    private static final String schemaExample = "{ \"name\": \"Test\", \"email\": \"test@test.com\", \"password\": \"Password123\" }";

    @Operation(summary = "Create a new user")
    @PostMapping("/api/user")
    public UserDTO createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample)))
            @RequestBody User user) throws Exception {
        return convertToDto(userService.saveUser(user));
    }

    @Operation(summary = "Get one user by id")
    @GetMapping("/api/user/{id}")
    public UserDTO getUser(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return convertToDto(user.get());
        } else {
            return null;
        }
    }

    @Operation(summary = "Get all users")
    @GetMapping("/api/user")
    public Map<String, ArrayList<UserDTO>> getUsers() {
        return convertIterableToDto(userService.getUsers());
    }

    @Operation(summary = "Update an existing user")
    @PutMapping("/api/user/{id}")
    public UserDTO updateUser(          
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

            return convertToDto(currentUser);
        } else {
            return null;
        }
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/api/user/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUser(id);
    }

    private UserDTO convertToDto(User user) {
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return userDTO;
	}

	private Map<String, ArrayList<UserDTO>> convertIterableToDto(Iterable<User> users) {
		ArrayList<UserDTO> usersDTO = new ArrayList<UserDTO>();

		for (User u : users) {
			UserDTO userDTO = modelMapper.map(u, UserDTO.class);
			usersDTO.add(userDTO);
		}
		
		Map<String, ArrayList<UserDTO>> map = new HashMap<>(); 
		map.put("users", usersDTO);

		return map;
	}
}
