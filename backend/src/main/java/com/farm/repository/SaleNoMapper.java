package com.farm.repository;

import com.farm.entity.SaleNo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SaleNoMapper {

    int getCount();

    int getMaxNO();

    SaleNo findByNo(@Param("no") String no);

    int insert(SaleNo saleNo);

    void delete(SaleNo saleNo);
}
