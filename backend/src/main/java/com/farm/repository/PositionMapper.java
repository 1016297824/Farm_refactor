package com.farm.repository;

import com.farm.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PositionMapper {

    List<Position> findAll();

    Position selectByPrimaryKey(@Param("id") int id);

    List<Position> findManagerAndStaff();

    List<Position> findAllFarmStaff();

    List<Position> findAllRestaurantStaff();

    int insert(Position position);

    int updateByPrimaryKey(Position position);
}
