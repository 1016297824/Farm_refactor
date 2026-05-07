package com.farm.repository;

import com.farm.entity.DiningTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiningTableMapper {

    List<DiningTable> findAll();

    DiningTable findById(@Param("id") int id);

    DiningTable selectByPrimaryKey(@Param("id") int id);

    int insert(DiningTable diningTable);

    int updateByPrimaryKey(DiningTable diningTable);

    int deleteByPrimaryKey(@Param("id") int id);
}
