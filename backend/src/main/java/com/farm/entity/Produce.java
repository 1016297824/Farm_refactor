package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// 生产
public class Produce {

    private int id;

    private LocalDateTime insertTime;

    private double amount;

    private Product product;
}
