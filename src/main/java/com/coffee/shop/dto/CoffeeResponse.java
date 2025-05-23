package com.coffee.shop.dto;

import java.io.Serializable;

import lombok.Builder;

public record CoffeeResponse(
    Long id,
    String name,
    String description,
    double price) implements Serializable {

    @Builder
    public static CoffeeResponse of(Long id, String name, String description, double price) {
        return new CoffeeResponse(id, name, description, price);
    }
}
