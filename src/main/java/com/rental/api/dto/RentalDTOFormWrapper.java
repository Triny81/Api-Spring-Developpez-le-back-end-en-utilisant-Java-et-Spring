package com.rental.api.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RentalDTOFormWrapper {
    private Long id;

    private String name;

    private Float surface;

    private Float price;

    private MultipartFile picture; // get the image send by the post request

    private String description;
}
