package com.colossus.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class Shop {

    private String id;

    private String name;

    private Date createTime;

    private Date updateTime;

}