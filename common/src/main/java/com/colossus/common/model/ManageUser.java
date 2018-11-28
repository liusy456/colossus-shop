package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ManageUser extends BaseModel {


    private static final long serialVersionUID = -127996929480943231L;
    /**
     * 用户名
     *
     * @mbg.generated
     */
    private String username;

    /**
     * 姓名
     *
     * @mbg.generated
     */
    private String name;

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

    /**
     * 工作
     *
     * @mbg.generated
     */
    private String job;

}