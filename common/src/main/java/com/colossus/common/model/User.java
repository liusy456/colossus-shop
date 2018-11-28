package com.colossus.common.model;

import lombok.Data;

@Data
public class User extends BaseModel {
    private static final long serialVersionUID = 3352801953526046427L;
    /**
     * 用户名
     *
     * @mbg.generated
     */
    private String username;

    /**
     * 密码，加密存储
     *
     * @mbg.generated
     */
    private String password;

    /**
     * 注册手机号
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 注册邮箱
     *
     * @mbg.generated
     */
    private String email;
}