package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthRolePermission extends BaseModel {

    private static final long serialVersionUID = 3558306869452938787L;
    /**
     * 角色编号
     *
     * @mbg.generated
     */
    private String roleId;

    /**
     * 权限编号
     *
     * @mbg.generated
     */
    private String permissionId;
}