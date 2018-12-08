package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthOrganization extends BaseModel {

    private static final long serialVersionUID = 7528958717297795888L;
    /**
     * 所属上级
     *
     * @mbg.generated
     */
    private String parentId;

    /**
     * 组织名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 组织描述
     *
     * @mbg.generated
     */
    private String description;

}