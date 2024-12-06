package com.rental.api.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

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

    private Date createdAt;

    private Date updatedAt;
}
