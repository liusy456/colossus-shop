package com.colossus.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Review {

    private String id;

    private String skuId;

    private String spuId;

    private String userId;

    private Integer likeCount;

    private BigDecimal star;

    private String img;

    private Date createTime;

    private Date updateTime;

    private String comment;
}