package com.farm.service;

import com.farm.entity.Ordering;
import com.farm.repository.OrderingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderingService {

    @Autowired
    private OrderingMapper orderingMapper;

    public List<Ordering> findByReserveNo(String no) {

        return orderingMapper.findByReserveNo(no);
    }

    public void saveAll(List<Ordering> orderingList) {

        for (Ordering ordering : orderingList) {
            orderingMapper.insert(ordering);
        }
    }
}
