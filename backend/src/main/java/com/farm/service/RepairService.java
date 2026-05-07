package com.farm.service;

import com.farm.entity.Repair;
import com.farm.repository.RepairMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RepairService {

    @Autowired
    private RepairMapper repairMapper;

    public void save(Repair repair) {

        if (repair.getId() != 0) {
            repairMapper.updateByPrimaryKey(repair);
        } else {
            repairMapper.insert(repair);
        }
    }

    public List<Repair> findByState(String repairState) {

        return repairMapper.findByState(repairState);
    }

    public void deleteRepair(Repair repair) {

        repairMapper.deleteByPrimaryKey(repair.getId());
    }

    public List<Repair> findByTime(LocalDateTime startTime, LocalDateTime endTime) {

        return repairMapper.findByTime(startTime, endTime);
    }
}
