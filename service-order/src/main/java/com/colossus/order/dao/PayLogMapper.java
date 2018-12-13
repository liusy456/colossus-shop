package com.colossus.order.dao;

import com.colossus.order.model.PayLog;

public interface PayLogMapper {

    int deleteByPrimaryKey(String id);

    int insert(PayLog record);

    int insertSelective(PayLog record);

    PayLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayLog record);

    int updateByPrimaryKey(PayLog record);
}