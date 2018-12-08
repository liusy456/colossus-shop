package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Item extends BaseModel {

    private static final long serialVersionUID = -1341141771194823140L;
    /**
     * 商品标题
     *
     * @mbg.generated
     */
    private String title;

    /**
     * 商品卖点
     *
     * @mbg.generated
     */
    private String sellPoint;

    /**
     * 商品价格，单位为：分
     *
     * @mbg.generated
     */
    private Long price;

    /**
     * 库存数量
     *
     * @mbg.generated
     */
    private Integer num;

    /**
     * 商品条形码
     *
     * @mbg.generated
     */
    private String barcode;

    /**
     * 商品图片
     *
     * @mbg.generated
     */
    private String image;

    /**
     * 所属类目，叶子类目
     *
     * @mbg.generated
     */
    private Long cid;

    /**
     * 商品状态，1-正常，2-下架，3-删除
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     * 重量
     *
     * @mbg.generated
     */
    private Integer weight;


    /**
     * 颜色
     *
     * @mbg.generated
     */
    private String colour;

    /**
     * 尺寸
     *
     * @mbg.generated
     */
    private String size;

}