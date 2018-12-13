package com.colossus.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class SkuSpec {

    private String id;

    private String spuId;

    private String specValueId;

    private Date createTime;

    private Date updateTime;

}