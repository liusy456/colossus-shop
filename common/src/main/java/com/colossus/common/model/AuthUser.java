package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthUser extends BaseModel {

    private static final long serialVersionUID = -44636749377387099L;
    /**
     * 帐号
     *
     * @mbg.generated
     */
    private String username;

    /**
     * 密码MD5(密码+盐)
     *
     * @mbg.generated
     */
    private String password;

    /**
     * 盐
     *
     * @mbg.generated
     */
    private String salt;

    /**
     * 姓名
     *
     * @mbg.generated
     */
    private String realname;

    /**
     * 头像
     *
     * @mbg.generated
     */
    private String avatar;

    /**
     * 电话
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 邮箱
     *
     * @mbg.generated
     */
    private String email;

    /**
     * 性别
     *
     * @mbg.generated
     */
    private Byte sex;

    /**
     * 状态(0:正常,1:锁定)
     *
     * @mbg.generated
     */
    private Byte locked=0;
}