package com.example.springbucks.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class NewOrderRequest {
    @NotEmpty
    private String customer;
    @NotEmpty
    private List<String> items;
}
