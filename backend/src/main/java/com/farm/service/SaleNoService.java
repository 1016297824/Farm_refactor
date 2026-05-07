package com.farm.service;

import com.farm.entity.SaleNo;
import com.farm.repository.SaleNoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleNoService {

    @Autowired
    private SaleNoMapper saleNoMapper;

    public int getCount() {

        return saleNoMapper.getCount();
    }

    public int getMaxNO() {

        return saleNoMapper.getMaxNO();
    }

    public void deleteSaleNo(SaleNo saleNo) {

        saleNoMapper.delete(saleNo);
    }

    public SaleNo findByNo(String no) {

        return saleNoMapper.findByNo(no);
    }

    public void save(SaleNo saleNo) {

        if (saleNo.getId() != 0) {
        } else {
            saleNoMapper.insert(saleNo);
        }
    }
}
