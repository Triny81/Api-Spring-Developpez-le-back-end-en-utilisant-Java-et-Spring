package com.rental.api.formWrapper;

import lombok.Data;

@Data
public class MessageFormWrapper {
    private Long id;

    private Long rental_id;

    private Long user_id;

    private String message;
}
