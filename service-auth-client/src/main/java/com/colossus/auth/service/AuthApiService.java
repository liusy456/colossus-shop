package com.colossus.auth.service;


import com.colossus.auth.service.hystrix.AuthApiServiceHystrix;
import com.colossus.common.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Auth系统接口
 * Created by Tlsy on 2017/2/11.
 */
@FeignClient(value = "service-auth",fallback = AuthApiServiceHystrix.class)
public interface AuthApiService {

    /**
     * 根据用户id获取所拥有的权限(用户和角色权限合集)
     * @param authUserId
     * @return
     */
    @GetMapping("select-permission-by-userId")
    List<AuthPermission> selectAuthPermissionByAuthUserId(String  authUserId);

    /**
     * 根据用户id获取所拥有的权限(用户和角色权限合集)
     * @param authUserId
     * @return
     */
    @GetMapping("select-permission-by-cache-userId")
    List<AuthPermission> selectAuthPermissionByAuthUserIdByCache(String  authUserId);

    /**
     * 根据用户id获取所属的角色
     * @param authUserId
     * @return
     */
    @GetMapping("select-role-by-userId")
    List<AuthRole> selectAuthRoleByAuthUserId(String authUserId);

    /**
     * 根据用户id获取所属的角色
     * @param authUserId
     * @return
     */
    @GetMapping("select-role-by-cache-userId")
    List<AuthRole> selectAuthRoleByAuthUserIdByCache(String  authUserId);

    /**
     * 根据角色id获取所拥有的权限
     * @param authRoleId
     * @return
     */
    @GetMapping("select-rolePermission-by-roleId")
    List<AuthRolePermission> selectAuthRolePermissionByAuthRoleId(String authRoleId);

    /**
     * 根据用户id获取所拥有的权限
     * @param authUserId
     * @return
     */
    @GetMapping("select-userPermission-by-userId")
    List<AuthUserPermission> selectAuthUserPermissionByAuthUserId(String authUserId);

    /**
     * 根据条件获取系统数据
     * @param authSystemExample
     * @return
     */
    @GetMapping("select-system-by-example")
    List<AuthSystem> selectAuthSystemByExample(AuthSystemExample authSystemExample);

    /**
     * 根据条件获取组织数据
     * @param authOrganizationExample
     * @return
     */
    @GetMapping("select-organization-by-example")
    List<AuthOrganization> selectAuthOrganizationByExample(AuthOrganizationExample authOrganizationExample);

    /**
     * 根据username获取AuthUser
     * @param username
     * @return
     */
    @GetMapping("select-user-by-userName")
    AuthUser selectAuthUserByUsername(String username);



}
