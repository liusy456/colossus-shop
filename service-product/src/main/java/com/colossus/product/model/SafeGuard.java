package com.colossus.product.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SafeGuard {

    private String id;

    private String name;

    private BigDecimal price;

    private Date createTime;

    private Date updateTime;
}