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

import com.rental.api.model.Message;
import com.rental.api.model.Rental;
import com.rental.api.model.User;
import com.rental.api.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

	private static final String schemaExample = "{ \"message\": \"A message\", \"rental\": { \"id\": 0}, \"user\": { \"id\": 0} }";

    @Operation(summary = "Add a message to a rental")
	@PostMapping("/message")
	public Message createMessage(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample)))
			@RequestBody Message message) {
		return messageService.saveMessage(message);
	}
	
	@Operation(summary = "Get one message by id")
	@GetMapping("/message/{id}")
	public Message getMessage(@PathVariable("id") final Long id) {
		Optional<Message> message = messageService.getMessage(id);
		if(message.isPresent()) {
			return message.get();
		} else {
			return null;
		}
	}
	
	@Operation(summary = "Get all messages")
	@GetMapping("/messages")
	public Iterable<Message> getMessages() {
		return messageService.getMessages();
	}
	
	@Operation(summary = "Update an existing message")
	@PutMapping("/message/{id}")
	public Message updateMessage(
		@PathVariable("id") final Long id, 
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample)))
		@RequestBody Message message) {
		Optional<Message> m = messageService.getMessage(id);

		if(m.isPresent()) {
			Message currentMessage = m.get();

			Rental rental = message.getRental();
			if(rental != null) {
				currentMessage.setRental(rental);
			}

			User user = message.getUser();
			if(user != null) {
				currentMessage.setUser(user);
			}

			String messageNew = message.getMessage();
			if(messageNew != null) {
				currentMessage.setMessage(messageNew);
			}

			messageService.saveMessage(currentMessage);

			return currentMessage;
		} else {
			return null;
		}
	}
	
	@Operation(summary = "Delete a message")
	@DeleteMapping("/message/{id}")
	public void deleteMessage(@PathVariable("id") final Long id) {
		messageService.deleteMessage(id);
	}
}
