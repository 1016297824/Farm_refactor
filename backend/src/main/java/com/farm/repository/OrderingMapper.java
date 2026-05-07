package com.farm.repository;

import com.farm.entity.Ordering;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderingMapper {

    List<Ordering> findByReserveNo(@Param("no") String no);

    Ordering selectByPrimaryKey(@Param("id") int id);

    int insert(Ordering ordering);

    int updateByPrimaryKey(Ordering ordering);

    int deleteByPrimaryKey(@Param("id") int id);
}
