package com.colossus.member.model;

import lombok.Data;

import java.util.Date;

@Data
public class ShippingAddress {

    private String id;

    private String province;

    private String city;

    private String area;

    private String address;

    private String contact;

    private String contactNumber;

    private Date createTime;

    private Date updateTime;
}