package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 餐厅物资表
public class RestaurantMaterial {

    private int id;

    private String name;

    private double amount;

    private double safeAmount;

    private String unit;

    private List<Purchase> purchaseList;
}
