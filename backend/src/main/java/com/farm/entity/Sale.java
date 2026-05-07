package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// 销售表
public class Sale {

    private int id;

    private double amount;

    private double price;

    private SaleNo saleNo;

    private Product product;
}
