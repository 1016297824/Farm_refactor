package com.farm.repository;

import com.farm.entity.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffMapper {

    List<Staff> findAll();

    Staff findByUsername(@Param("username") String username);

    List<Staff> findByPositionAuthorityIsNull();

    List<Staff> findByPositionAuthorityManagerStaff();

    int getCount();

    int getMaxNo();

    List<Staff> findAllFarmStaff();

    List<Staff> findAllRestaurantStaff();

    List<Staff> findAllWorked();

    List<Staff> findAllManager();

    int insert(Staff staff);

    int updateByPrimaryKey(Staff staff);

    int deleteByPrimaryKey(@Param("id") int id);

    Staff selectByPrimaryKey(@Param("id") int id);
}
