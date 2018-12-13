package com.colossus.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class SpuSpec {

    private String id;

    private String spuId;

    private String specId;

    private Date createTime;

    private Date updateTime;

}