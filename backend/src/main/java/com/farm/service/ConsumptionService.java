package com.farm.service;

import com.farm.entity.Consumption;
import com.farm.repository.ConsumptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConsumptionService {

    @Autowired
    private ConsumptionMapper consumptionMapper;

    public void save(Consumption consumption) {

        consumptionMapper.insert(consumption);
    }
}
