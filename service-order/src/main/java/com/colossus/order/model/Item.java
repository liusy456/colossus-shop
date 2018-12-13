package com.colossus.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品
 */
@Data
public class Item {

    private String id;

    private String orderNo;

    private String skuId;

    private String spuId;

    private String shopId;

    private String productName;

    private Integer productCount;

    private BigDecimal productPrice;

    private Date createTime;

    private Date updateTime;
}