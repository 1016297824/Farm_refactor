package com.farm.repository;

import com.farm.entity.Purchase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PurchaseMapper {

    List<Purchase> findByTime(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);

    int insert(Purchase purchase);
}
