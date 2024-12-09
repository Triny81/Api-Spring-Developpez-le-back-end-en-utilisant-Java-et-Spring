package com.rental.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.api.formWrapper.MessageFormWrapper;
import com.rental.api.model.Message;
import com.rental.api.model.Rental;
import com.rental.api.model.User;
import com.rental.api.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    public Optional<Message> getMessage(final Long id) {
        return messageRepository.findById(id);
    }

    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    public void deleteMessage(final Long id) {
        messageRepository.deleteById(id);
    }

    public Message saveMessage(MessageFormWrapper messageWrapper) throws Exception {
        Message message = messageWrapper.getId() == null ? new Message() : getMessage(messageWrapper.getId()).get();

        Optional<Rental> rental = rentalService.getRental(messageWrapper.getRental_id());
        Optional<User> user = userService.getUser(messageWrapper.getUser_id());

        if (!rental.isPresent()) {
            throw new Exception("Rental not found");
        } 

        if (!user.isPresent()) {
            throw new Exception("User not found");
        } 

        message.setRental(rental.get());
        message.setUser(user.get());
        message.setMessage(messageWrapper.getMessage());

        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }
}
