package com.colossus.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class SpecValue {

    private String id;

    private String specId;

    private String specValue;

    private Date createTime;

    private Date updateTime;

}