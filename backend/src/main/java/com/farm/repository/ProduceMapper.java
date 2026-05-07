package com.farm.repository;

import com.farm.entity.Produce;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProduceMapper {

    int insert(Produce produce);
}
