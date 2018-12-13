package com.colossus.order.dao;

import com.colossus.order.model.Logistics;

public interface LogisticsMapper {

    int deleteByPrimaryKey(String id);

    int insert(Logistics record);

    int insertSelective(Logistics record);

    Logistics selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Logistics record);

    int updateByPrimaryKey(Logistics record);
}