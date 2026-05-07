package com.farm.repository;

import com.farm.entity.OrderingComplete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderingCompleteMapper {

    List<OrderingComplete> findByTime(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    OrderingComplete selectByPrimaryKey(@Param("id") int id);

    int insert(OrderingComplete orderingComplete);

    int updateByPrimaryKey(OrderingComplete orderingComplete);

    int deleteByPrimaryKey(@Param("id") int id);
}
