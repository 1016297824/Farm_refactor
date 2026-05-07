package com.farm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
// 用户
public class Customer {

    private int id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    private List<Reserve> reserveList;

    private List<OrderingComplete> completeList;

    public Customer(String username, String password, String name) {

        this.username = username;
        this.password = password;
        this.name = name;
    }
}
