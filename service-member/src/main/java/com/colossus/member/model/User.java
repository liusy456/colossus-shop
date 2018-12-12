package com.colossus.member.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private String id;

    private String phone;

    private String email;

    private String avatar;

    private String name;

    private String password;

    private Date createTime;

    private Date updateTime;

    private String defaultShippingAddress;

}