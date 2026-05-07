package com.farm.service;

import com.farm.entity.DiningTable;
import com.farm.repository.DiningTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiningTableService {

    @Autowired
    private DiningTableMapper diningTableMapper;

    public List<DiningTable> findAll() {

        return diningTableMapper.findAll();
    }

    public DiningTable findById(int id){

        return diningTableMapper.findById(id);
    }

    public void initDiningTable() {

        List<DiningTable> diningTableList = new ArrayList<DiningTable>();

        for (int i = 0; i < 12; i++) {
            DiningTable diningTable = new DiningTable("4人桌", 4);
            diningTableList.add(diningTable);
        }

        for (int i = 0; i < 8; i++) {
            DiningTable diningTable = new DiningTable("8人桌", 8);
            diningTableList.add(diningTable);
        }

        for (int i = 0; i < 4; i++) {
            DiningTable diningTable = new DiningTable("12人桌", 12);
            diningTableList.add(diningTable);
        }

        for (DiningTable dt : diningTableList) {
            diningTableMapper.insert(dt);
        }
    }
}
