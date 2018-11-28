package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ItemParamItem extends BaseModel {

    private static final long serialVersionUID = -3995028810242669363L;
    /**
     * 商品ID
     *
     * @mbg.generated
     */
    private String itemId;

    /**
     * 参数数据，格式为json格式
     *
     * @mbg.generated
     */
    private String paramData;
}