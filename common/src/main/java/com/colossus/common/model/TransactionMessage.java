package com.colossus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class TransactionMessage extends BaseModel {

    private static final long serialVersionUID = -6586605741909967020L;
    /**
     * 版本号
     *
     * @mbg.generated
     */
    private Integer version;

    /**
     * 修改者
     *
     * @mbg.generated
     */
    private String editor;

    /**
     * 创建者
     *
     * @mbg.generated
     */
    private String creater;

    /**
     * 最后修改时间
     *
     * @mbg.generated
     */
    private Date editTime;


    /**
     * 消息ID
     *
     * @mbg.generated
     */
    private String messageId;

    /**
     * 消息数据类型
     *
     * @mbg.generated
     */
    private String messageDataType;

    /**
     * 消费队列
     *
     * @mbg.generated
     */
    private String consumerQueue;

    /**
     * 消息重发次数
     *
     * @mbg.generated
     */
    private Short messageSendTimes;

    /**
     * 是否死亡
     *
     * @mbg.generated
     */
    private String areadlyDead;

    /**
     * 状态
     *
     * @mbg.generated
     */
    private String status;

    /**
     * 备注
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 扩展字段1
     *
     * @mbg.generated
     */
    private String field1;

    /**
     * 扩展字段2
     *
     * @mbg.generated
     */
    private String field2;

    /**
     * 扩展字段3
     *
     * @mbg.generated
     */
    private String field3;


    /**
     * 消息内容
     *
     * @mbg.generated
     */
    private String messageBody;

}