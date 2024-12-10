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

import com.rental.api.dto.MessageDTO;
import com.rental.api.formWrapper.MessageFormWrapper;
import com.rental.api.model.Message;
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

	private static final String schemaExample = "{ \"message\": \"A message\", \"rental_id\": \"0\", \"user_id\": \"0\" }";

    @Operation(summary = "Send a message to the owner of a rental")
	@PostMapping("/api/messages")
	public String createMessage(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The message is linked to the rental and to the owner of the rental", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageFormWrapper.class), examples = @ExampleObject(value = schemaExample)))
			@RequestBody MessageFormWrapper message) throws Exception {
		
		messageService.saveMessage(message);
		return "{ \"message\": \"Message send with success\" }";
	}
	
	@Operation(summary = "Get one message by id")
	@GetMapping("/api/messages/{id}")
	public MessageDTO getMessage(@PathVariable("id") final Long id) {
		Optional<Message> message = messageService.getMessage(id);
		if(message.isPresent()) {
			return convertToDto(message.get());
		} else {
			return null;
		}
	}
	
	@Operation(summary = "Get all messages")
	@GetMapping("/api/messages")
	public Map<String, ArrayList<MessageDTO>> getMessages() {
		return convertIterableToDto(messageService.getMessages());
	}
	
	@Operation(summary = "Update an existing message")
	@PutMapping("/api/messages/{id}")
	public String updateMessage(
		@PathVariable("id") final Long id, 
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageFormWrapper.class), examples = @ExampleObject(value = schemaExample)))
		@RequestBody MessageFormWrapper message) throws Exception {
		Optional<Message> m = messageService.getMessage(id);
		
		if(m.isPresent()) {
			Message currentMessage = m.get();
			message.setId(id);

			if(message.getRental_id() == null) {
				message.setRental_id(currentMessage.getRental().getId());
			}

			if(message.getUser_id() == null) {
				message.setUser_id(currentMessage.getUser().getId());
			}

			if (message.getMessage() == null) {
				message.setMessage(currentMessage.getMessage());
			}

			messageService.saveMessage(message);
			return "{ \"message\": \"Message updated with success\" }";
	
		} else {
			return null;
		}
	}
	
	@Operation(summary = "Delete a message")
	@DeleteMapping("/api/messages/{id}")
	public void deleteMessage(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class), examples = @ExampleObject(value = schemaExample))) @PathVariable("id") final Long id) {
		messageService.deleteMessage(id);
	}

	private MessageDTO convertToDto(Message message) {
		MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
		return messageDTO;
	}

	private Map<String, ArrayList<MessageDTO>> convertIterableToDto(Iterable<Message> messages) {
		ArrayList<MessageDTO> messagesDTO = new ArrayList<MessageDTO>();

		for (Message m : messages) {
			MessageDTO messageDTO = modelMapper.map(m, MessageDTO.class);
			messagesDTO.add(messageDTO);
		}
		
		Map<String, ArrayList<MessageDTO>> map = new HashMap<>(); 
		map.put("messages", messagesDTO);

		return map;
	}
}
