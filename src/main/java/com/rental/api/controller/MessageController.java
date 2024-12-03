package com.rental.api.controller;

import java.util.ArrayList;
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

import com.rental.api.dto.MessageDTO;
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

	@Autowired
    private ModelMapper modelMapper;

	private static final String schemaExample = "{ \"message\": \"A message\", \"rental\": { \"id\": 0}, \"user\": { \"id\": 0} }";

    @Operation(summary = "Add a message to a rental")
	@PostMapping("/message")
	public MessageDTO createMessage(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class), examples = @ExampleObject(value = schemaExample)))
			@RequestBody Message message) {
		return convertToDto(messageService.saveMessage(message));
	}
	
	@Operation(summary = "Get one message by id")
	@GetMapping("/message/{id}")
	public MessageDTO getMessage(@PathVariable("id") final Long id) {
		Optional<Message> message = messageService.getMessage(id);
		if(message.isPresent()) {
			return convertToDto(message.get());
		} else {
			return null;
		}
	}
	
	@Operation(summary = "Get all messages")
	@GetMapping("/messages")
	public ArrayList<MessageDTO> getMessages() {
		return convertIterableToDto(messageService.getMessages());
	}
	
	@Operation(summary = "Update an existing message")
	@PutMapping("/message/{id}")
	public MessageDTO updateMessage(
		@PathVariable("id") final Long id, 
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class), examples = @ExampleObject(value = schemaExample)))
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

			return convertToDto(currentMessage);
		} else {
			return null;
		}
	}
	
	@Operation(summary = "Delete a message")
	@DeleteMapping("/message/{id}")
	public void deleteMessage(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class), examples = @ExampleObject(value = schemaExample))) @PathVariable("id") final Long id) {
		messageService.deleteMessage(id);
	}

	private MessageDTO convertToDto(Message message) {
		MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
		return messageDTO;
	}

	private ArrayList<MessageDTO> convertIterableToDto(Iterable<Message> messages) {
		ArrayList<MessageDTO> messagesDTO = new ArrayList<MessageDTO>();

		for (Message m : messages) {
			MessageDTO messageDTO = modelMapper.map(m, MessageDTO.class);
			messagesDTO.add(messageDTO);
		}
		
		return messagesDTO;
	}
}
