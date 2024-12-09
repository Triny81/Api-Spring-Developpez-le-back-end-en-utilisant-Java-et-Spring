package com.rental.api.formWrapper;

import org.springframework.web.multipart.MultipartFile;

import com.rental.api.model.User;

import lombok.Data;

@Data
public class RentalFormWrapper {
    private Long id;

    private String name;

    private Float surface;

    private Float price;

    private MultipartFile picture; // get the image send by the post request

    private String description;

    private User owner;
}
