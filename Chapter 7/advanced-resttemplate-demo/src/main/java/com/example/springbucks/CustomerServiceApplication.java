package com.example.springbucks;

import com.example.springbucks.model.Coffee;
import com.example.springbucks.support.CustomConnectionKeepAliveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class CustomerServiceApplication implements ApplicationRunner {

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(CustomerServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory requestFactory() {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        connectionManager.setMaxTotal(200); // 最大200个连接
        connectionManager.setDefaultMaxPerRoute(20); // 每个Route最大20个连接

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictIdleConnections(30, TimeUnit.SECONDS) // idle 连接30秒关掉
                .disableAutomaticRetries() // 关闭自动重试
                // 有 Keep-Alive 认里面的值，没有的话永久有效
                //.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                // 换成自定义的
                .setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        return requestFactory;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/coffee/?name={name}")
                .build("mocha");
        RequestEntity<Void> req = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .build();

//        ResponseEntity<Coffee> c = restTemplate.getForEntity(uri, Coffee.class);
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
        log.info("Response Status: {}, Response Headers: {}", resp.getStatusCode(), resp.getHeaders().toString());
        log.info("Coffee: {}", resp.getBody());

        String coffeeUri = "http://localhost:8080/coffee/";
        Coffee request = Coffee.builder()
                .name("Americano")
                .price(Money.of(CurrencyUnit.of("CNY"), 25.00))
                .build();
        Coffee response = restTemplate.postForObject(coffeeUri, request, Coffee.class);
        log.info("New Coffee: {}", response);

        ParameterizedTypeReference<List<Coffee>> ptr =
                new ParameterizedTypeReference<List<Coffee>>() {};
        ResponseEntity<List<Coffee>> list = restTemplate.exchange(coffeeUri, HttpMethod.GET, null, ptr);
        list.getBody().forEach(c -> log.info("Coffee: {}", c));
//        String s = restTemplate.getForObject(coffeeUri, String.class);
//        log.info("String: {}", s);
    }
}
