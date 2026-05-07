package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// 采购表
public class Purchase {

    private int id;

    private LocalDateTime insertTime;

    private double amount;

    private double price;

    private Product product;

    private Fertilizer fertilizer;

    private RestaurantMaterial restaurantMaterial;
}
