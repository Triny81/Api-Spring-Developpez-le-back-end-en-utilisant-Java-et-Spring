package com.rental.api.dto;

import lombok.Data;

@Data
public class MessageDTOFormWrapper {
    private Long id;

    private Long rental_id;

    private Long user_id;

    private String message;
}
