package com.rental.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MessageDTO {
    private Long id;

    private RentalDTO rental; 

    private UserDTO user; 

    private String message;

    private Date createdAt;

    private Date updatedAt;
}
