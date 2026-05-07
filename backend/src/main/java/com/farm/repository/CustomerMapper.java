package com.farm.repository;

import com.farm.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<Customer> findAll();

    Customer findByUsername(@Param("username") String username);

    int insert(Customer customer);

    int updateByPrimaryKey(Customer customer);

    Customer selectByPrimaryKey(@Param("id") int id);
}
