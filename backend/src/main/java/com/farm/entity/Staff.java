package com.farm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 员工
public class Staff {

    private int id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    private String telNumber;

    @Email
    private String email;

    private String address;

    private String idCardNo;

    private Position position;

    private List<Attendance> attendanceList;

    public Staff(String username, String password, String name, String telNumber, String email, String address, String idCardNo, Position position) {

        this.username = username;
        this.password = password;
        this.name = name;
        this.telNumber = telNumber;
        this.email = email;
        this.address = address;
        this.idCardNo = idCardNo;
        this.position = position;
    }
}
