package com.colossus.product.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Sku {

    private String id;

    private String skuNo;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String shopId;

    private String spuId;

    private Date createTime;

    private Date updateTime;

}