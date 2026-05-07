package com.farm.repository;

import com.farm.entity.Consumption;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsumptionMapper {

    int insert(Consumption consumption);
}
