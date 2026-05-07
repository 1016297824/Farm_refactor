package com.farm.service;

import com.farm.entity.Purchase;
import com.farm.repository.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    public void save(Purchase purchase) {

        purchaseMapper.insert(purchase);
    }

    public List<Purchase> findByTime(LocalDateTime startTime, LocalDateTime endTime) {

        return purchaseMapper.findByTime(startTime, endTime);
    }
}
