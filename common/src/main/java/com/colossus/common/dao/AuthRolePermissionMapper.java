package com.colossus.common.dao;

import com.colossus.common.model.AuthRolePermission;
import com.colossus.common.model.AuthRolePermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthRolePermissionMapper {
    long countByExample(AuthRolePermissionExample example);

    int deleteByExample(AuthRolePermissionExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthRolePermission record);

    int insertSelective(AuthRolePermission record);

    List<AuthRolePermission> selectByExample(AuthRolePermissionExample example);

    AuthRolePermission selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthRolePermission record, @Param("example") AuthRolePermissionExample example);

    int updateByExample(@Param("record") AuthRolePermission record, @Param("example") AuthRolePermissionExample example);

    int updateByPrimaryKeySelective(AuthRolePermission record);

    int updateByPrimaryKey(AuthRolePermission record);
}