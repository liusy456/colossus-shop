package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CategoryImage extends BaseModel {

    private static final long serialVersionUID = -3895581295978203306L;
    /**
     * 分类id
     *
     * @mbg.generated
     */
    private String cid;

    /**
     * 名字
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 网址或网页id
     *
     * @mbg.generated
     */
    private String url;

    /**
     * 状态。可选值:1(little),2(big)
     *
     * @mbg.generated
     */
    private Integer littleOrBig;

    /**
     * 状态。可选值:1(正常),2(删除)
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 图片路径
     *
     * @mbg.generated
     */
    private String imageUrl;

    /**
     * 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
     *
     * @mbg.generated
     */
    private Integer sortOrder;
}