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

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
	 * Create - Add a new message
	 * @param message An object message
	 * @return The message object saved
	 */
	@PostMapping("/message")
	public Message createMessage(@RequestBody Message message) {
		return messageService.saveMessage(message);
	}
	
	/**
	 * Read - Get one message 
	 * @param id The id of the message
	 * @return An Message object full filled
	 */
	@GetMapping("/message/{id}")
	public Message getMessage(@PathVariable("id") final Long id) {
		Optional<Message> message = messageService.getMessage(id);
		if(message.isPresent()) {
			return message.get();
		} else {
			return null;
		}
	}
	
	/**
	 * Read - Get all messages
	 * @return - An Iterable object of Message full filled
	 */
	@GetMapping("/messages")
	public Iterable<Message> getMessages() {
		return messageService.getMessages();
	}
	
	/**
	 * Update - Update an existing message
	 * @param id - The id of the message to update
	 * @param message - The message object updated
	 * @return
	 */
	@PutMapping("/message/{id}")
	public Message updateMessage(@PathVariable("id") final Long id, @RequestBody Message message) {
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
	
	
	/**
	 * Delete - Delete an message
	 * @param id - The id of the message to delete
	 */
	@DeleteMapping("/message/{id}")
	public void deleteMessage(@PathVariable("id") final Long id) {
		messageService.deleteMessage(id);
	}
}
