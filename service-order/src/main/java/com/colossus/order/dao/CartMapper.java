package com.colossus.order.dao;

import com.colossus.order.model.Cart;

public interface CartMapper {

    int deleteByPrimaryKey(String id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}