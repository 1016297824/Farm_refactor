package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 单号表
public class SaleNo {

    private int id;

    private String no;

    private LocalDateTime insertTime;

    private List<Sale> saleList;
}
