package com.farm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// 考勤表
public class Attendance {

    private int id;

    private LocalDateTime insertTime;

    private double workingHours;

    private Staff staff;
}
