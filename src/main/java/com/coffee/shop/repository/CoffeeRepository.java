package com.coffee.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coffee.shop.model.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
