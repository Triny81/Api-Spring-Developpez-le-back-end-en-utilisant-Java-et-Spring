package com.rental.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String name;

    private Date createdAt;

    private Date updatedAt;
}
