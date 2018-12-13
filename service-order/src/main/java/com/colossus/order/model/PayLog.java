package com.colossus.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayLog {

    private String id;

    private String orderNo;

    private String payType;

    private BigDecimal totalAmount;

    private BigDecimal actualAmount;

    private String remark;

    private String payInfo;

    private Date createTime;

    private Date updateTime;

}