package com.coffee.shop.service;

import com.coffee.shop.dto.CoffeeRequest;
import com.coffee.shop.dto.CoffeeResponse;
import com.coffee.shop.exception.CoffeeNotFoundException;
import com.coffee.shop.model.Coffee;
import com.coffee.shop.repository.CoffeeRepository;
import com.coffee.shop.util.CoffeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CoffeeServiceTest {
    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private CoffeeMapper coffeeMapper;
    
    @InjectMocks
    private CoffeeService coffeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCoffees_shouldReturnList() {
        Coffee coffee = new Coffee();
        CoffeeResponse response = CoffeeResponse.of(null, null, null, 0.0);
        when(coffeeRepository.findAll()).thenReturn(Arrays.asList(coffee));
        when(coffeeMapper.toCoffeeResponse(coffee)).thenReturn(response);
        List<CoffeeResponse> result = coffeeService.getAllCoffees(0, 10, "id", "asc");
        assertEquals(1, result.size());
        verify(coffeeRepository).findAll();
    }

    @Test
    void getCoffeeById_found() {
        Coffee coffee = new Coffee();
        CoffeeResponse response = CoffeeResponse.of(null, null, null, 0.0);
        when(coffeeRepository.findById(1L)).thenReturn(Optional.of(coffee));
        when(coffeeMapper.toCoffeeResponse(coffee)).thenReturn(response);
        CoffeeResponse result = coffeeService.getCoffeeById(1L);
        assertNotNull(result);
    }

    @Test
    void getCoffeeById_notFound() {
        when(coffeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CoffeeNotFoundException.class, () -> coffeeService.getCoffeeById(1L));
    }

    @Test
    void createCoffee_success() {
        CoffeeRequest request = new CoffeeRequest(null, null, 0.0);
        Coffee coffee = new Coffee();
        CoffeeResponse response = CoffeeResponse.of(null, null, null, 0.0);
        when(coffeeMapper.toCoffee(request)).thenReturn(coffee);
        when(coffeeRepository.save(coffee)).thenReturn(coffee);
        when(coffeeMapper.toCoffeeResponse(coffee)).thenReturn(response);
        CoffeeResponse result = coffeeService.createCoffee(request);
        assertNotNull(result);
    }

    @Test
    void updateCoffee_success() {
        CoffeeRequest request = new CoffeeRequest(null, null, 0.0);
        Coffee coffee = new Coffee();
        CoffeeResponse response = CoffeeResponse.of(null, null, null, 0.0);
        when(coffeeRepository.existsById(1L)).thenReturn(true);
        when(coffeeMapper.toCoffee(request)).thenReturn(coffee);
        when(coffeeRepository.save(any(Coffee.class))).thenReturn(coffee);
        when(coffeeMapper.toCoffeeResponse(coffee)).thenReturn(response);
        CoffeeResponse result = coffeeService.updateCoffee(1L, request);
        assertNotNull(result);
    }

    @Test
    void updateCoffee_notFound() {
        CoffeeRequest request = new CoffeeRequest(null, null, 0.0);
        when(coffeeRepository.existsById(1L)).thenReturn(false);
        assertThrows(CoffeeNotFoundException.class, () -> coffeeService.updateCoffee(1L, request));
    }

    @Test
    void deleteCoffee_success() {
        Coffee coffee = new Coffee();
        when(coffeeRepository.findById(1L)).thenReturn(Optional.of(coffee));
        doNothing().when(coffeeRepository).deleteById(1L);
        assertDoesNotThrow(() -> coffeeService.deleteCoffee(1L));
        verify(coffeeRepository).deleteById(1L);
    }

    @Test
    void deleteCoffee_notFound() {
        when(coffeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CoffeeNotFoundException.class, () -> coffeeService.deleteCoffee(1L));
    }

    @Test
    void clearCoffeeCache_shouldNotThrow() {
        assertDoesNotThrow(() -> coffeeService.clearCoffeeCache());
    }

    @Test
    void clearAllCoffeesCache_shouldNotThrow() {
        assertDoesNotThrow(() -> coffeeService.clearAllCoffeesCache());
    }
}
