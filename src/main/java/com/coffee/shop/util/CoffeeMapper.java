package com.coffee.shop.util;

import org.springframework.stereotype.Component;

import com.coffee.shop.dto.CoffeeRequest;
import com.coffee.shop.dto.CoffeeResponse;
import com.coffee.shop.model.Coffee;

@Component
public class CoffeeMapper {

    public Coffee toCoffee(CoffeeRequest coffeeRequest) {
        return Coffee.builder()
                .name(coffeeRequest.name())
                .description(coffeeRequest.description())
                .price(coffeeRequest.price())
                .build();
    }
    
    public CoffeeResponse toCoffeeResponse(Coffee coffee) {
        return CoffeeResponse.builder()
                .id(coffee.getId())
                .name(coffee.getName())
                .description(coffee.getDescription())
                .price(coffee.getPrice())
                .build();
    }
}
