package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ItemDesc extends BaseModel {
    private static final long serialVersionUID = -4834987119367783344L;
    /**
     * 商品ID
     *
     * @mbg.generated
     */
    private String itemId;
    /**
     * 商品描述
     *
     * @mbg.generated
     */
    private String itemDesc;

}