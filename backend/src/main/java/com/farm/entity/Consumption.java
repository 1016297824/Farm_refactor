package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// 异常消耗
public class Consumption {

    private int id;

    private LocalDateTime insertTime;

    private double amount;

    private Product product;
}
