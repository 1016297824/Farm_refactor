package com.farm.service;

import com.farm.entity.Attendance;
import com.farm.repository.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    public List<Attendance> findAll() {

        return attendanceMapper.findAll();
    }

    public void saveAll(List<Attendance> attendanceList) {

        for (Attendance attendance : attendanceList) {
            attendanceMapper.insert(attendance);
        }
    }

    public List<Attendance> findManagerByChooseTime(LocalDateTime chooseDate1, LocalDateTime chooseDate2) {

        return attendanceMapper.findManagerByChooseTime(chooseDate1, chooseDate2);
    }

    public List<Attendance> findFarmStaffByChooseTime(LocalDateTime chooseDate1, LocalDateTime chooseDate2) {

        return attendanceMapper.findFarmStaffByChooseTime(chooseDate1, chooseDate2);
    }

    public List<Attendance> findRestaurantStaffByChooseTime(LocalDateTime chooseDate1, LocalDateTime chooseDate2) {

        return attendanceMapper.findRestaurantStaffByChooseTime(chooseDate1, chooseDate2);
    }
}
