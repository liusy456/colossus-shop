package com.colossus.order.model;

import lombok.Data;

import java.util.Date;

/**
 * 订单物流追踪
 */
@Data
public class LogisticsTracking {

    private String id;

    private String orderNo;

    private String logisticsCompanyNo;

    private String trackingNo;

    private String remark;

    private Date createTime;

    private Date updateTime;

}