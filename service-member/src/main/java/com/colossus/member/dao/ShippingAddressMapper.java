package com.colossus.member.dao;

import com.colossus.member.model.ShippingAddress;

public interface ShippingAddressMapper {

    int deleteByPrimaryKey(String id);

    int insert(ShippingAddress record);

    int insertSelective(ShippingAddress record);

    ShippingAddress selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShippingAddress record);

    int updateByPrimaryKey(ShippingAddress record);
}