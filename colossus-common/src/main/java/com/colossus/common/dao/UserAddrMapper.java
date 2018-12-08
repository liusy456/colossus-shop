package com.colossus.common.dao;

import com.colossus.common.model.UserAddr;
import com.colossus.common.model.UserAddrExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAddrMapper {
    long countByExample(UserAddrExample example);

    int deleteByExample(UserAddrExample example);

    int deleteByPrimaryKey(String id);

    int insert(UserAddr record);

    int insertSelective(UserAddr record);

    List<UserAddr> selectByExample(UserAddrExample example);

    UserAddr selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UserAddr record, @Param("example") UserAddrExample example);

    int updateByExample(@Param("record") UserAddr record, @Param("example") UserAddrExample example);

    int updateByPrimaryKeySelective(UserAddr record);

    int updateByPrimaryKey(UserAddr record);
}