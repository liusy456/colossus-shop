package com.colossus.order.dao;

import com.colossus.order.model.Item;

public interface ItemMapper {

    int deleteByPrimaryKey(String id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
}