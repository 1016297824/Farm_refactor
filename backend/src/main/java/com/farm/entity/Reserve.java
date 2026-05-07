package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 预定表
public class Reserve {

    private int id;

    private String no;

    private LocalDateTime insertTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Customer customer;

    private DiningTable diningTable;

    private List<Ordering> orderList;
}
