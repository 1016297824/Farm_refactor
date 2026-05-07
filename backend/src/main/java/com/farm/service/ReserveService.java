package com.farm.service;

import com.farm.entity.DiningTable;
import com.farm.entity.Reserve;
import com.farm.repository.ReserveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReserveService {

    @Autowired
    private ReserveMapper reserveMapper;

    public List<Reserve> findByCustomerUsername(String username) {

        return reserveMapper.findByCustomerUsername(username);
    }

    public Reserve findByNo(String no) {

        return reserveMapper.findByNo(no);
    }

    public void deleteReserve(Reserve reserve) {

        reserveMapper.deleteByPrimaryKey(reserve.getId());
    }

    public List<DiningTable> findDiningTableByTime(
            LocalDateTime startTime,
            LocalDateTime endTime) {

        return reserveMapper.findDiningTableByTime(startTime, endTime);
    }

    public int getCount() {

        return reserveMapper.getCount();
    }

    public int getMaxNo() {

        return reserveMapper.getMaxNo();
    }

    public void save(Reserve reserve) {

        if (reserve.getId() != 0) {
            reserveMapper.updateByPrimaryKey(reserve);
        } else {
            reserveMapper.insert(reserve);
        }
    }

    public List<Reserve> findAllOverdueReserve(LocalDateTime nowTime) {

        return reserveMapper.findAllOverdueReserve(nowTime);
    }

    public List<Reserve> findAll() {

        return reserveMapper.findAll();
    }

    public List<Reserve> findAllCustomerNotNull() {

        return reserveMapper.findAllCustomerNotNull();
    }
}
