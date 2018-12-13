package com.colossus.order.model;

import lombok.Data;

import java.util.Date;

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