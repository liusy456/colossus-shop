package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthUserRole extends BaseModel {

    private static final long serialVersionUID = 8022952561699960741L;
    /**
     * 用户编号
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * 角色编号
     *
     * @mbg.generated
     */
    private String roleId;

}