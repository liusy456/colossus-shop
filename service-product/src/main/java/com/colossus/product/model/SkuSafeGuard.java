package com.colossus.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class SkuSafeGuard {

    private String id;

    private String skuId;

    private String safeGuardId;

    private Date createTime;

    private Date updateTime;

}