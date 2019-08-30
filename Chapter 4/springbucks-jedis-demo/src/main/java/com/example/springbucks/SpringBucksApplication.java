package com.example.springbucks;

import com.example.springbucks.model.Coffee;
import com.example.springbucks.model.CoffeeOrder;
import com.example.springbucks.model.OrderState;
import com.example.springbucks.repository.CoffeeRepository;
import com.example.springbucks.service.CoffeeOrderService;
import com.example.springbucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info(jedisPoolConfig.toString());

		try (Jedis jedis = jedisPool.getResource()) {
			coffeeService.findAllCoffee().forEach(c -> {
			});
		};
	}
}
