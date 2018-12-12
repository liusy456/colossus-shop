package com.colossus.member.model;

import lombok.Data;

import java.util.Date;

@Data
public class Certification {

    private String id;

    private String name;

    private String sex;

    private String nationality;

    private Date birthday;

    private String address;

    private String idCard;

    private String idCardFront;

    private String idCardBack;

    private String idCardHand;

    private Date createTime;

    private Date updateTime;
}