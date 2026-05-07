package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 饲料肥料表
public class Fertilizer {

    public static final String fertilizerType1 = "饲料";
    public static final String fertilizerType2 = "肥料";

    private int id;

    private String name;

    private double amount;

    private double safeAmount;

    private String unit;

    private String fertilizerType;

    private List<Purchase> purchaseList;

    public Fertilizer(String name, double amount, double safeAmount, String unit, String fertilizerType) {

        this.name = name;
        this.amount = amount;
        this.safeAmount = safeAmount;
        this.unit = unit;
        this.fertilizerType = fertilizerType;
    }
}
