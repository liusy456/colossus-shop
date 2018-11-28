package com.colossus.common.dao;

import com.colossus.common.model.AuthUserPermission;
import com.colossus.common.model.AuthUserPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthUserPermissionMapper {
    long countByExample(AuthUserPermissionExample example);

    int deleteByExample(AuthUserPermissionExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthUserPermission record);

    int insertSelective(AuthUserPermission record);

    List<AuthUserPermission> selectByExample(AuthUserPermissionExample example);

    AuthUserPermission selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthUserPermission record, @Param("example") AuthUserPermissionExample example);

    int updateByExample(@Param("record") AuthUserPermission record, @Param("example") AuthUserPermissionExample example);

    int updateByPrimaryKeySelective(AuthUserPermission record);

    int updateByPrimaryKey(AuthUserPermission record);
}