package com.farm.repository;

import com.farm.entity.Sale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SaleMapper {

    List<Sale> findByTime(@Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);

    List<Sale> findByNo(@Param("no") String no);

    int insert(Sale sale);
}
