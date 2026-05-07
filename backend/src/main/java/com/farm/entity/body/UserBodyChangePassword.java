package com.farm.entity.body;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 用户修改密码
public class UserBodyChangePassword {

    private String username;
    private String password;
    private String newPassword;
    private String newPassword1;
}
