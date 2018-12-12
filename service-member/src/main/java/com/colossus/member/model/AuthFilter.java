package com.colossus.member.model;

import lombok.Data;

import java.util.Date;

@Data
public class AuthFilter {

    private String id;

    private String url;

    private String filter;

    private Integer serviceId;

    private Date createTime;

    private Date updateTime;
}