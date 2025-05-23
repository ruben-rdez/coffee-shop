package com.coffee.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoffeeRequest(
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    String name,
    
    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank") 
    String description,
     
    Double price) {

}
