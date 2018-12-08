package com.colossus.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.colossus.auth.service.AuthApiService;
import com.colossus.common.dao.*;
import com.colossus.common.model.*;
import com.colossus.common.utils.StringUtil;
import com.colossus.redis.service.RedisService;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "authApi-service",description = "权限基本服务")
@RefreshScope
public class AuthApiServiceImpl implements AuthApiService {
    private static final Logger logger= LoggerFactory.getLogger(AuthApiServiceImpl.class);
    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private AuthUserPermissionMapper authUserPermissionMapper;
    @Autowired
    private AuthRolePermissionMapper authRolePermissionMapper;
    @Autowired
    private AuthSystemMapper authSystemMapper;
    @Autowired
    private AuthOrganizationMapper authOrganizationMapper;
    @Autowired
    private RedisService redisService;
    @Override
    public List<AuthPermission> selectAuthPermissionByAuthUserId(String  authUserId) {
        AuthUser user=authUserMapper.selectByPrimaryKey(authUserId);
        if(user==null||1==user.getLocked()){
            logger.error("用户不存在或已被锁定userId:{}",authUserId);
            return null;
        }
        List<AuthPermission> authPermissions=authUserMapper.selectAuthPermissionByAuthUserId(authUserId);
        redisService.hset("auth","selectAuthPermissionByAuthUserId_"+authUserId, JSON.toJSONString(authPermissions));
        return authPermissions;
    }

    @Override
    public List<AuthPermission> selectAuthPermissionByAuthUserIdByCache(String authUserId) {
        String authPermissions=redisService.hget("auth","selectAuthPermissionByAuthUserId_"+authUserId);
        if(StringUtil.isEmpty(authPermissions)){
            return selectAuthPermissionByAuthUserId(authUserId);
        }
        return JSON.parseArray(authPermissions,AuthPermission.class);
    }

    @Override
    public List<AuthRole> selectAuthRoleByAuthUserId(String authUserId) {
        AuthUser user=authUserMapper.selectByPrimaryKey(authUserId);
        if(user==null||1==user.getLocked()){
            logger.error("用户不存在或已被锁定userId:{}",authUserId);
            return null;
        }
        List<AuthRole> authRoles=authUserMapper.selectAuthRoleByAuthUserId(authUserId);
        redisService.hset("auth","selectAuthRoleByAuthUserId_"+authUserId,JSON.toJSONString(authRoles));
        return authRoles;
    }

    @Override
    public List<AuthRole> selectAuthRoleByAuthUserIdByCache(String authUserId) {
        String  authRoles=redisService.hget("auth","selectAuthRoleByAuthUserId_"+authUserId);
        if(StringUtil.isEmpty(authRoles)){
            return selectAuthRoleByAuthUserId(authUserId);
        }
        return JSON.parseArray(authRoles,AuthRole.class);
    }

    @Override
    public List<AuthRolePermission> selectAuthRolePermissionByAuthRoleId(String authRoleId) {
        AuthRolePermissionExample example=new AuthRolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(authRoleId);
        return authRolePermissionMapper.selectByExample(example);
    }

    @Override
    public List<AuthUserPermission> selectAuthUserPermissionByAuthUserId(String authUserId) {
        AuthUserPermissionExample example=new AuthUserPermissionExample();
        example.createCriteria().andUserIdEqualTo(authUserId);
        return authUserPermissionMapper.selectByExample(example);
    }

    @Override
    public List<AuthSystem> selectAuthSystemByExample(AuthSystemExample authSystemExample) {
        return authSystemMapper.selectByExample(authSystemExample);
    }

    @Override
    public List<AuthOrganization> selectAuthOrganizationByExample(AuthOrganizationExample authOrganizationExample) {
        return authOrganizationMapper.selectByExample(authOrganizationExample);
    }

    @Override
    public AuthUser selectAuthUserByUsername(String username) {
        AuthUserExample example=new AuthUserExample();
        example.createCriteria().andUsernameEqualTo(username);
       List<AuthUser> authUsers=authUserMapper.selectByExample(example);
       if(CollectionUtils.isNotEmpty(authUsers)){
           return authUsers.get(0);
       }
       return null;
    }


}
