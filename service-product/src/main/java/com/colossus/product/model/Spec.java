package com.colossus.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class Spec {

    private String id;

    private String specNo;

    private String name;

    private Date createTime;

    private Date updateTime;
}