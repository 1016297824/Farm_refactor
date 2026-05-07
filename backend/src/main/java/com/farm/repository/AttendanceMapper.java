package com.farm.repository;

import com.farm.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AttendanceMapper {

    List<Attendance> findAll();

    List<Attendance> findManagerByChooseTime(@Param("chooseDate1") LocalDateTime chooseDate1,
                                             @Param("chooseDate2") LocalDateTime chooseDate2);

    List<Attendance> findFarmStaffByChooseTime(@Param("chooseDate1") LocalDateTime chooseDate1,
                                               @Param("chooseDate2") LocalDateTime chooseDate2);

    List<Attendance> findRestaurantStaffByChooseTime(@Param("chooseDate1") LocalDateTime chooseDate1,
                                                     @Param("chooseDate2") LocalDateTime chooseDate2);

    int insert(Attendance attendance);
}
