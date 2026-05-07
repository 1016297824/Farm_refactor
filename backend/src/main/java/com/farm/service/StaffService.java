package com.farm.service;

import com.farm.entity.Staff;
import com.farm.repository.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private PositionService positionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Staff> findAll() {

        return staffMapper.findAll();
    }

    public Staff findByUsername(String username) {

        return staffMapper.findByUsername(username);
    }

    public List<Staff> findByPositionAuthorityIsNull() {

        return staffMapper.findByPositionAuthorityIsNull();
    }

    public List<Staff> findByPositionAuthorityManagerStaff() {

        return staffMapper.findByPositionAuthorityManagerStaff();
    }

    public List<Staff> findAllFarmStaff() {

        return staffMapper.findAllFarmStaff();
    }

    public List<Staff> findAllRestaurantStaff() {

        return staffMapper.findAllRestaurantStaff();
    }

    public int getCount() {

        return staffMapper.getCount();
    }

    public int getMaxNo() {

        return staffMapper.getMaxNo();
    }

    public void save(Staff staff) {

        if (staff.getId() != 0) {
            staffMapper.updateByPrimaryKey(staff);
        } else {
            staffMapper.insert(staff);
        }
    }

    public void deleteStaff(Staff staff) {

        staffMapper.deleteByPrimaryKey(staff.getId());
    }

    public List<Staff> findAllWorked() {

        return staffMapper.findAllWorked();
    }

    public void updateStaff(Staff staff) {

        staffMapper.updateByPrimaryKey(staff);
    }

    public List<Staff> findAllManager() {

        return staffMapper.findAllManager();
    }

    public void intiStaff() {

        List<Staff> staffList = new ArrayList<Staff>();

        for (int i = 0; i < positionService.findAll().size(); i++) {
            Staff staff = new Staff("1000" + (i + 1), passwordEncoder.encode("123456"), "zk", "123456789", "zk" + (i + 1) + "@qq.com", "天津市静海区", "1234567890", positionService.findById(i + 1));
            staffList.add(staff);
        }

        for (Staff staff : staffList) {
            staffMapper.insert(staff);
        }
    }
}
