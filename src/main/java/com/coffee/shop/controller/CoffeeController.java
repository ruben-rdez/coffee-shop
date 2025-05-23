package com.coffee.shop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffee.shop.dto.CoffeeRequest;
import com.coffee.shop.dto.CoffeeResponse;
import com.coffee.shop.service.CoffeeService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping
    @Operation(summary = "Get all coffees", description = "Get all coffees from the coffee shop")
    public ResponseEntity<List<CoffeeResponse>> getAllCoffees(
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy,
                                        @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(coffeeService.getAllCoffees(page, size, sortBy, sortDirection));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coffee by ID", description = "Get a specific coffee by its ID")
    public ResponseEntity<CoffeeResponse> getCoffeeById(@PathVariable Long id) {
        CoffeeResponse coffeeResponse = coffeeService.getCoffeeById(id);
        if (coffeeResponse != null) {
            return ResponseEntity.ok(coffeeResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new coffee", description = "Create a new coffee in the coffee shop")
    public ResponseEntity<CoffeeResponse> createCoffee(@Valid @RequestBody CoffeeRequest coffeeRequest) {
        CoffeeResponse coffeeResponse = coffeeService.createCoffee(coffeeRequest);
        return ResponseEntity.status(201).body(coffeeResponse);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing coffee", description = "Update an existing coffee in the coffee shop")
    public ResponseEntity<CoffeeResponse> updateCoffee(
                                                    @PathVariable Long id, 
                                                    @Valid @RequestBody CoffeeRequest coffeeRequest) {
        CoffeeResponse coffeeResponse = coffeeService.updateCoffee(id, coffeeRequest);
        if (coffeeResponse != null) {
            return ResponseEntity.ok(coffeeResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a coffee", description = "Delete a specific coffee by its ID")
    public ResponseEntity<Void> deleteCoffee(@PathVariable Long id) {
        coffeeService.deleteCoffee(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/cache/clear")
    @Operation(summary = "Clear coffee cache", description = "Clear the coffee cache")
    public ResponseEntity<Void> clearCache() {
        coffeeService.clearCoffeeCache();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cache/clear/coffeesPaginated")
    @Operation(summary = "Clear all coffees cache", description = "Clear the all coffees cache")
    public ResponseEntity<Void> clearUsersPaginatedCache() {
        coffeeService.clearAllCoffeesCache();
        return ResponseEntity.ok().build();
    }
}
