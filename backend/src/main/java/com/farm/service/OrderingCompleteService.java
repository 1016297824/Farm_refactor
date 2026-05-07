package com.farm.service;

import com.farm.entity.OrderingComplete;
import com.farm.repository.OrderingCompleteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderingCompleteService {

    @Autowired
    private OrderingCompleteMapper orderingCompleteMapper;

    public void save(OrderingComplete orderingComplete) {

        orderingCompleteMapper.insert(orderingComplete);
    }

    public List<OrderingComplete> findByTime(LocalDateTime startTime, LocalDateTime endTime) {

        return orderingCompleteMapper.findByTime(startTime, endTime);
    }
}
