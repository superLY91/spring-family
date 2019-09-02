package com.example.springbucks.service;

import com.example.springbucks.model.CoffeeOrder;
import com.example.springbucks.repository.CoffeeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    @Autowired
    private CoffeeOrderRepository repository;

    public Mono<Long> create(CoffeeOrder order) {
        return repository.save(order);
    }
}
