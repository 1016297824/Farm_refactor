package com.farm.repository;

import com.farm.entity.Repair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RepairMapper {

    List<Repair> findByState(@Param("repairState") String repairState);

    List<Repair> findByTime(@Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime);

    Repair selectByPrimaryKey(@Param("id") int id);

    int insert(Repair repair);

    int updateByPrimaryKey(Repair repair);

    int deleteByPrimaryKey(@Param("id") int id);
}
