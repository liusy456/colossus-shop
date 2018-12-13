package com.colossus.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {

    private String id;

    private String orderNo;

    private Integer itemCount;

    private String userId;

    private String shopId;

    private String payType;

    private Date orderTime;

    private Date payTime;

    private String tradeNo;

    private String shipType;

    private Date expectArriveTime;

    private BigDecimal totalAmount;

    private BigDecimal shipAmount;

    private BigDecimal actualAmount;

    private String status;

    private Date createTime;

    private Date updateTime;
}