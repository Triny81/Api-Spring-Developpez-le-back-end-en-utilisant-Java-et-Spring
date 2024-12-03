package com.rental.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RentalDTO {
    private Long id;

    private String name;

    private Float surface;

    private Float price;

    private String picture;

    private String description;

    private UserDTO owner;

    private Date createdAt;

    private Date updatedAt;

}
