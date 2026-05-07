package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Position {

    public static final String superManager = "SuperManager";
    public static final String farmManager = "FarmManager";
    public static final String restaurantManager = "RestaurantManager";
    public static final String farmStaff = "FarmStaff";
    public static final String restaurantStaff = "RestaurantStaff";

    private int id;

    private String name;

    private String location;

    private double basicSalary;

    private String authority;

    public Position(String name, String location, double basicSalary, String authority) {
        this.name = name;
        this.location = location;
        this.basicSalary = basicSalary;
        this.authority = authority;
    }
}
