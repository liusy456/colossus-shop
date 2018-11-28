package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CategorySecondary extends BaseModel {

    private static final long serialVersionUID = -6121846304775956171L;
    /**
     * 分类id
     *
     * @mbg.generated
     */
    private String cid;

    /**
     * 父类目ID=0时，代表的是一级的类目 ID=-1时表示是cid上部分类
     *
     * @mbg.generated
     */
    private Long parentId;

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
     * 状态。可选值:1(正常),2(删除)
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
     *
     * @mbg.generated
     */
    private Integer sortOrder;

    /**
     * 该类目是否为父类目，1为true，0为false
     *
     * @mbg.generated
     */
    private Boolean isParent;


}