package com.colossus.order.model;

import lombok.Data;

import java.util.Date;

/**
 * 订单物流信息
 */
@Data
public class Logistics {

    private String id;

    private String orderNo;

    private String logisticsCompanyId;

    private String logisticsCompanyNo;

    private String trackingNo;

    private Date deliveryTime;

    private Date arrivalTime;

    private Date createTime;

    private Date updateTime;
}