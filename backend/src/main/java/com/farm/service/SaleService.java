package com.farm.service;

import com.farm.entity.Sale;
import com.farm.repository.SaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleMapper saleMapper;

    public void saveAll(List<Sale> saleList) {

        for (Sale sale : saleList) {
            saleMapper.insert(sale);
        }
    }

    public List<Sale> findByTime(LocalDateTime startTime, LocalDateTime endTime) {

        return saleMapper.findByTime(startTime, endTime);
    }

    public List<Sale> findByNo(String no) {

        return saleMapper.findByNo(no);
    }
}
