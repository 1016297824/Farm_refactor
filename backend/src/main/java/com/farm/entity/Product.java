package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 产品
public class Product {

    public static final String productType1 = "家禽";
    public static final String productType2 = "鱼类";
    public static final String productType3 = "果蔬";

    private int id;

    private String name;

    private String unit;

    private double amount;

    private double safeAmount;

    private String productType;

    private double baseAmount;

    private String baseUnit;

    private List<Consumption> consumptionList;

    private List<Produce> produceList;

    private List<Purchase> purchaseList;

    private List<Sale> saleList;

    public Product(String name, String unit, double amount, double safeAmount, String productType, double baseAmount, String baseUnit) {

        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.safeAmount = safeAmount;
        this.productType = productType;
        this.baseAmount = baseAmount;
        this.baseUnit = baseUnit;
    }
}
