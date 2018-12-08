package com.colossus.common.dao;

import com.colossus.common.model.AuthUserAddr;
import com.colossus.common.model.AuthUserAddrExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthUserAddrMapper {
    long countByExample(AuthUserAddrExample example);

    int deleteByExample(AuthUserAddrExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthUserAddr record);

    int insertSelective(AuthUserAddr record);

    List<AuthUserAddr> selectByExample(AuthUserAddrExample example);

    AuthUserAddr selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthUserAddr record, @Param("example") AuthUserAddrExample example);

    int updateByExample(@Param("record") AuthUserAddr record, @Param("example") AuthUserAddrExample example);

    int updateByPrimaryKeySelective(AuthUserAddr record);

    int updateByPrimaryKey(AuthUserAddr record);
}