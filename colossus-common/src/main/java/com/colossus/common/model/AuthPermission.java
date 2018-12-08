package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuthPermission extends BaseModel {

    private static final long serialVersionUID = -6566801210577025821L;
    /**
     * 所属系统
     *
     * @mbg.generated
     */
    private String systemId;

    /**
     * 所属上级
     *
     * @mbg.generated
     */
    private String parentId;

    /**
     * 名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 类型(1:目录,2:菜单,3:按钮)
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     * 权限值
     *
     * @mbg.generated
     */
    private String permissionValue;

    /**
     * 路径
     *
     * @mbg.generated
     */
    private String uri;

    /**
     * 图标
     *
     * @mbg.generated
     */
    private String icon;

    /**
     * 状态(0:禁止,1:正常)
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     * 排序
     *
     * @mbg.generated
     */
    private Long orders;

}