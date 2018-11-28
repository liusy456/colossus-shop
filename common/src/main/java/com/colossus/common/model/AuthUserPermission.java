package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthUserPermission extends BaseModel {

    private static final long serialVersionUID = -8417570970966192593L;
    /**
     * 用户编号
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * 权限编号
     *
     * @mbg.generated
     */
    private String permissionId;

    /**
     * 权限类型(-1:减权限,1:增权限)
     *
     * @mbg.generated
     */
    private Byte type;
}