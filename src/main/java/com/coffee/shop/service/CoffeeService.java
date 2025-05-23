package com.coffee.shop.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffee.shop.dto.CoffeeRequest;
import com.coffee.shop.dto.CoffeeResponse;
import com.coffee.shop.model.Coffee;
import com.coffee.shop.repository.CoffeeRepository;
import com.coffee.shop.util.CoffeeMapper;
import com.coffee.shop.exception.CoffeeNotFoundException;

@Service
public class CoffeeService {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeService.class);

    private final CoffeeRepository coffeeRepository;

    private final CoffeeMapper coffeeMapper;

    public CoffeeService(CoffeeRepository coffeeRepository, CoffeeMapper coffeeMapper) {
        this.coffeeMapper = coffeeMapper;
        this.coffeeRepository = coffeeRepository;
    }

    @Cacheable(value = "ALL_COFFEES_CACHE",
            key = "{#page, #size, #sortBy, #sortDirection}",
            unless = "#result == null || #result.isEmpty()")
    public List<CoffeeResponse> getAllCoffees(int page, int size, String sortBy, String sortDirection) {
        logger.info("Fetching all coffees from the database");
        return coffeeRepository.findAll()
                .stream()
                .map(coffeeMapper::toCoffeeResponse)
                .toList();
    }

    @Cacheable(value = "COFFEE_CACHE", key = "#id", condition = "#id != null")
    public CoffeeResponse getCoffeeById(Long id) {
        logger.info("Fetching coffee with id: {}", id);
        return coffeeRepository.findById(id)
            .map(coffeeMapper::toCoffeeResponse)
            .orElseThrow(() -> {
                logger.error("Coffee not found with ID: {}", id);
                return new CoffeeNotFoundException(
                    String.format("Coffee with ID %s not found", id));
            });
    }

    @CacheEvict(value = "ALL_COFFEES_CACHE", allEntries = true)
    public CoffeeResponse createCoffee(CoffeeRequest coffeeRequest) {
        logger.info("Creating new coffee: {}", coffeeRequest);
        Coffee coffee = coffeeRepository.save(coffeeMapper.toCoffee(coffeeRequest));
        return coffeeMapper.toCoffeeResponse(coffee);
               
    }

    @CacheEvict(value = {"ALL_COFFEES_CACHE", "COFFEE_CACHE"}, key = "#id", 
        condition = "#id != null", allEntries = true)
    public CoffeeResponse updateCoffee(Long id, CoffeeRequest coffeeRequest) {
        logger.info("Updating coffee with id: {}", id);
        if (coffeeRepository.existsById(id)){
            Coffee coffee = coffeeMapper.toCoffee(coffeeRequest);
            coffee.setId(id);
            return coffeeMapper.toCoffeeResponse(coffeeRepository.save(coffee));
        }
        logger.error("Coffee not found with ID: {}", id);
        throw new CoffeeNotFoundException(
            String.format("Coffee with ID %s not found", id));
    }

    @CacheEvict(value = {"ALL_COFFEES_CACHE", "COFFEE_CACHE"}, key = "#id", 
        condition = "#id != null", allEntries = true)
    public void deleteCoffee(Long id) {
        logger.info("Deleting coffee with id: {}", id);
        coffeeRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Coffee not found with ID: {}", id);
                return new CoffeeNotFoundException(
                    String.format("Coffee with ID %s not found", id));
            });
        coffeeRepository.deleteById(id);
    }

    @CacheEvict(value = "COFFEE_CACHE", allEntries = true)
    public void clearCoffeeCache() {
        logger.warn("Clearing coffees cache");
    }

    @CacheEvict(value = "ALL_COFFEES_CACHE", allEntries = true)
    public void clearAllCoffeesCache() {
        logger.info("Clearing all coffees cache");
    }
}
