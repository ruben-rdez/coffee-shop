package com.coffee.shop.exception;

public class CoffeeNotFoundException extends RuntimeException {
    public CoffeeNotFoundException(String message){
        super(message);
    }
}
