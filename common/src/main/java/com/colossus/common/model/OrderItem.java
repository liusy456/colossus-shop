package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderItem extends BaseModel {

    /**
     * 商品id
     *
     * @mbg.generated
     */
    private String itemId;

    /**
     * 订单id
     *
     * @mbg.generated
     */
    private String orderId;

    /**
     * 商品购买数量
     *
     * @mbg.generated
     */
    private Integer num;

    /**
     * 商品标题
     *
     * @mbg.generated
     */
    private String title;

    /**
     * 商品单价
     *
     * @mbg.generated
     */
    private Long price;

    /**
     * 商品总金额
     *
     * @mbg.generated
     */
    private Long totalFee;

    /**
     * 商品图片地址
     *
     * @mbg.generated
     */
    private String picPath;

    /**
     * 总重量 单位/克
     *
     * @mbg.generated
     */
    private String weights;

}