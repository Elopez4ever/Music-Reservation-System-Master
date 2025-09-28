package com.bookingsystem.dto;

import lombok.Data;

/**
 * 功能：
 * 作者： lapis
 * 邮箱： lapis17@qq.com
 */
@Data
public class UserLoginDTO {
    private String username;
    private String password;
    private String captcha;
}
