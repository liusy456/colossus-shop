package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class HotWords extends BaseModel {
    private static final long serialVersionUID = 5327463665508727065L;
    /**
     * 热门词分类 1-加红 2-搜索框显示 null-正常
     *
     * @mbg.generated
     */
    private String category;

    /**
     * 热门词
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 跳转网页
     *
     * @mbg.generated
     */
    private String url;

    /**
     * 热门词状态，1-搜索，2-指定网页
     *
     * @mbg.generated
     */
    private Byte search;
}