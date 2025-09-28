package com.bookingsystem.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 功能：
 * 作者： lapis
 * 邮箱： lapis17@qq.com
 */
@Data
@Builder
public class RegisterInfoDTO {

    /**
     *  用户名
     */
    private String username;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 学院id
     */
    private Long deptId;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     *
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String emailCode;


    /**
     * 验证码
     */
    private String captchaCode;
}

