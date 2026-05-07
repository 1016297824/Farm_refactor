package com.farm.repository;

import com.farm.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    List<Product> findByProductType(@Param("productType") String productType);

    Product findByName(@Param("name") String name);

    Product selectByPrimaryKey(@Param("id") int id);

    int insert(Product product);

    int updateByPrimaryKey(Product product);

    int deleteByPrimaryKey(@Param("id") int id);
}
