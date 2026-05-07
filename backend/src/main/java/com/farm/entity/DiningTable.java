package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 餐桌
public class DiningTable {

    private int id;

    private String type;

    private int seat;

    private List<Reserve> reserveList;

    public DiningTable(String type, int seat) {

        this.type = type;
        this.seat = seat;
    }
}
