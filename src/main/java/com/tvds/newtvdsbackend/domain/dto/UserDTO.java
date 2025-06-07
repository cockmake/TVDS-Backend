package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    @NotEmpty(message = "用户名不能为空")
    String username;
    @NotEmpty(message = "密码不能为空")
    String password;
}
