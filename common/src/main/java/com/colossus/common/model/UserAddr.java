package com.colossus.common.model;

import lombok.Data;

@Data
public class UserAddr extends BaseModel {

    private static final long serialVersionUID = -2062202485277491009L;
    /**
     * 用户ID
     *
     * @mbg.generated
     */
    private String userId;

    /**
     * 收货人全名
     *
     * @mbg.generated
     */
    private String receiverName;

    /**
     * 固定电话
     *
     * @mbg.generated
     */
    private String receiverPhone;

    /**
     * 移动电话
     *
     * @mbg.generated
     */
    private String receiverMobile;

    /**
     * 省份
     *
     * @mbg.generated
     */
    private String receiverState;

    /**
     * 城市
     *
     * @mbg.generated
     */
    private String receiverCity;

    /**
     * 区/县
     *
     * @mbg.generated
     */
    private String receiverDistrict;

    /**
     * 收货地址，如：xx路xx号
     *
     * @mbg.generated
     */
    private String receiverAddress;

    /**
     * 邮政编码,如：110000
     *
     * @mbg.generated
     */
    private String receiverZip;

}