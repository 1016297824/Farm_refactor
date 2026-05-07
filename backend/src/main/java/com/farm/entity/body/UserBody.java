package com.farm.entity.body;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 用户登录
public class UserBody {

    private String username;
    private String password;
}
