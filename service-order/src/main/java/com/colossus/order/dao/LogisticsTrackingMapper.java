package com.colossus.order.dao;

import com.colossus.order.model.LogisticsTracking;

public interface LogisticsTrackingMapper {

    int deleteByPrimaryKey(String id);

    int insert(LogisticsTracking record);

    int insertSelective(LogisticsTracking record);

    LogisticsTracking selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(LogisticsTracking record);

    int updateByPrimaryKey(LogisticsTracking record);
}