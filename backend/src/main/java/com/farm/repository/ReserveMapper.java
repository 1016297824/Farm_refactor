package com.farm.repository;

import com.farm.entity.DiningTable;
import com.farm.entity.Reserve;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReserveMapper {

    List<Reserve> findByCustomerUsername(@Param("username") String username);

    Reserve findByNo(@Param("no") String no);

    List<DiningTable> findDiningTableByTime(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    int getCount();

    int getMaxNo();

    List<Reserve> findAllOverdueReserve(@Param("nowTime") LocalDateTime nowTime);

    List<Reserve> findAll();

    List<Reserve> findAllCustomerNotNull();

    Reserve selectByPrimaryKey(@Param("id") int id);

    int insert(Reserve reserve);

    int updateByPrimaryKey(Reserve reserve);

    int deleteByPrimaryKey(@Param("id") int id);
}
