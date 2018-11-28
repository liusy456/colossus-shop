package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthUserOrganization extends BaseModel {

    private static final long serialVersionUID = -6612782632987012397L;
    /**
     * 用户编号
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * 组织编号
     *
     * @mbg.generated
     */
    private String organizationId;
}