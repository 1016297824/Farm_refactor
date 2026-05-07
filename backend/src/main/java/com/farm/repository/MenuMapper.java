package com.farm.repository;

import com.farm.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<Menu> findAll();

    Menu selectByPrimaryKey(@Param("id") int id);

    int insert(Menu menu);

    int updateByPrimaryKey(Menu menu);

    int deleteByPrimaryKey(@Param("id") int id);
}
