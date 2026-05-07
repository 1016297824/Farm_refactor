package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// 员工
public class OrderingComplete {

    private int id;

    private LocalDateTime insertTime;

    private String reserveNo;

    private double totalPrice;

    private Customer customer;
}
