package com.farm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Repair {

    public static final String repairType1 = "报修";
    public static final String RepairType2 = "报损";

    public static final String state1 = "未完成";
    public static final String state2 = "已完成";

    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertTime;

    private String repairType;

    private String content;

    private String cause;

    private String state;

    private double price;
}
