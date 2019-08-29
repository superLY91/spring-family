package com.example.springbucks.repository;

import com.example.springbucks.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeOrderRespository extends JpaRepository<CoffeeOrder, Long> {
}
