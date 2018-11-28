package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class Order extends BaseModel {

    private static final long serialVersionUID = -2531743856163786157L;
    /**
     * 用户id
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * 地址id
     *
     * @mbg.generated
     */
    private String addrId;

    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     *
     * @mbg.generated
     */
    private String payment;

    /**
     * 支付类型，1、货到付款，2、在线支付，3、微信支付，4、支付宝支付
     *
     * @mbg.generated
     */
    private Integer paymentType;

    /**
     * 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
     *
     * @mbg.generated
     */
    private String postFee;

    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 物流名称
     *
     * @mbg.generated
     */
    private String shippingName;

    /**
     * 物流单号
     *
     * @mbg.generated
     */
    private String shippingCode;

    /**
     * 退换无忧
     *
     * @mbg.generated
     */
    private String noAnnoyance;

    /**
     * 服务费
     *
     * @mbg.generated
     */
    private String servicePrice;

    /**
     * 返现
     *
     * @mbg.generated
     */
    private String returnPrice;

    /**
     * 订单总重 单位/克
     *
     * @mbg.generated
     */
    private String totalWeight;

    /**
     * 买家是否已经评价
     *
     * @mbg.generated
     */
    private Integer buyerRate;

    /**
     * 交易关闭时间
     *
     * @mbg.generated
     */
    private Date closeTime;

    /**
     * 交易完成时间
     *
     * @mbg.generated
     */
    private Date endTime;

    /**
     * 付款时间
     *
     * @mbg.generated
     */
    private Date paymentTime;

    /**
     * 发货时间
     *
     * @mbg.generated
     */
    private Date consignTime;
}