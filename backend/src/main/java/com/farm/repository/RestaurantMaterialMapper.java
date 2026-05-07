package com.farm.repository;

import com.farm.entity.RestaurantMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RestaurantMaterialMapper {

    List<RestaurantMaterial> findAll();

    RestaurantMaterial findByName(@Param("name") String name);

    RestaurantMaterial selectByPrimaryKey(@Param("id") int id);

    int insert(RestaurantMaterial restaurantMaterial);

    int updateByPrimaryKey(RestaurantMaterial restaurantMaterial);

    int deleteByPrimaryKey(@Param("id") int id);
}
