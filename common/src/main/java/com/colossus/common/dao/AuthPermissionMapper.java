package com.colossus.common.dao;

import com.colossus.common.model.AuthPermission;
import com.colossus.common.model.AuthPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthPermissionMapper {
    long countByExample(AuthPermissionExample example);

    int deleteByExample(AuthPermissionExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthPermission record);

    int insertSelective(AuthPermission record);

    List<AuthPermission> selectByExample(AuthPermissionExample example);

    AuthPermission selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthPermission record, @Param("example") AuthPermissionExample example);

    int updateByExample(@Param("record") AuthPermission record, @Param("example") AuthPermissionExample example);

    int updateByPrimaryKeySelective(AuthPermission record);

    int updateByPrimaryKey(AuthPermission record);
}