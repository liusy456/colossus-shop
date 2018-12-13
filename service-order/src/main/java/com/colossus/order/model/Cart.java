package com.colossus.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车
 */
@Data
public class Cart {

    private String id;

    private String skuId;

    private String spuId;

    private String userId;

    private String shopId;

    private String productName;

    private Integer productCount;

    private BigDecimal productPrice;

    private String status;

    private Date createTime;

    private Date updateTime;

}