package com.farm.repository;

import com.farm.entity.Fertilizer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FertilizerMapper {

    List<Fertilizer> findAll();

    List<Fertilizer> findByFertilizerType(@Param("fertilizerType") String fertilizerType);

    Fertilizer findByName(@Param("name") String name);

    Fertilizer selectByPrimaryKey(@Param("id") int id);

    int insert(Fertilizer fertilizer);

    int updateByPrimaryKey(Fertilizer fertilizer);

    int deleteByPrimaryKey(@Param("id") int id);
}
