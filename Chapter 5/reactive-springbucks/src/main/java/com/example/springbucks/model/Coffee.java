package com.example.springbucks.model;

import com.example.springbucks.serializer.MoneyDeserializer;
import com.example.springbucks.serializer.MoneySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table("t_coffee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coffee {
    @Id
    private String id;
    private String name;
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private Money price;
    private Date createTime;
    private Date updateTime;
}