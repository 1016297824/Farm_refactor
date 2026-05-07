package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 菜单
public class Menu {

    private int id;

    private String name;

    private double price;

    private String unite;

    private String type;

    private List<Ordering> orderingList;

    public Menu(String name, double price, String unite, String type) {

        this.name = name;
        this.price = price;
        this.unite = unite;
        this.type = type;
    }
}
