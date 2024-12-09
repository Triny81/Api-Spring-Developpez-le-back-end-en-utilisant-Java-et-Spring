package com.rental.api.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RentalDTO {
    private Long id;

    private String name;

    private Float surface;

    private Float price;

    private String picture;

    private String description;

    @JsonProperty("owner_id")
    private Long ownerId;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date createdAt;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date updatedAt;
}
