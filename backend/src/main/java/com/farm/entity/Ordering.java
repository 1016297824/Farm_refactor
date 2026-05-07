package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 点餐表
public class Ordering {

    private int id;

    private int count;

    private Reserve reserve;

    private Menu menu;
}
