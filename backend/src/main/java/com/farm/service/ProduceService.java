package com.farm.service;

import com.farm.entity.Produce;
import com.farm.repository.ProduceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProduceService {

    @Autowired
    private ProduceMapper produceMapper;

    public void save(Produce produce) {

        produceMapper.insert(produce);
    }
}
