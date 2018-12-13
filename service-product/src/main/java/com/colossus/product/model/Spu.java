package com.colossus.product.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Spu {

    private String id;

    private String spuNo;

    private String name;

    private BigDecimal lowPrice;

    private String categoryId;

    private String brandId;

    private Date createTime;

    private Date updateTime;

}