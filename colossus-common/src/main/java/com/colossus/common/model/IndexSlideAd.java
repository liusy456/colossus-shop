package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class IndexSlideAd extends BaseModel {

    private static final long serialVersionUID = 8419201594154241694L;
    /**
     * 提示
     *
     * @mbg.generated
     */
    private String alt;

    /**
     * log号
     *
     * @mbg.generated
     */
    private String clog;

    /**
     * 无
     *
     * @mbg.generated
     */
    private String ext1;

    /**
     * 网址
     *
     * @mbg.generated
     */
    private String href;

    /**
     * log等级
     *
     * @mbg.generated
     */
    private String logv;

    /**
     * 大图片
     *
     * @mbg.generated
     */
    private String src;

    /**
     * 小图片
     *
     * @mbg.generated
     */
    private String srcb;

    /**
     * 状态。可选值:1(正常),2(删除)
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 排序
     *
     * @mbg.generated
     */
    private Integer sortOrder;

    /**
     * log记录网址前缀
     *
     * @mbg.generated
     */
    private String logdomain;
}