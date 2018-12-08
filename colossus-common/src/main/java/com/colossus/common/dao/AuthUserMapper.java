package com.colossus.common.dao;

import com.colossus.common.model.AuthPermission;
import com.colossus.common.model.AuthRole;
import com.colossus.common.model.AuthUser;
import com.colossus.common.model.AuthUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthUserMapper {
    long countByExample(AuthUserExample example);

    int deleteByExample(AuthUserExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthUser record);

    int insertSelective(AuthUser record);

    List<AuthUser> selectByExample(AuthUserExample example);

    AuthUser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthUser record, @Param("example") AuthUserExample example);

    int updateByExample(@Param("record") AuthUser record, @Param("example") AuthUserExample example);

    int updateByPrimaryKeySelective(AuthUser record);

    int updateByPrimaryKey(AuthUser record);

    List<AuthPermission> selectAuthPermissionByAuthUserId(String authUserId);

    List<AuthRole> selectAuthRoleByAuthUserId(String authUserId);
}