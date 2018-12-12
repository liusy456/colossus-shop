package com.colossus.member.service;

import com.alibaba.fastjson.JSONArray;
import com.colossus.member.service.hystrix.AuthRolePermissionServiceHystrix;
import com.colossus.common.model.AuthRolePermission;
import com.colossus.common.model.AuthRolePermissionExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
* AuthRolePermissionService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthRolePermissionServiceHystrix.class)
public interface AuthRolePermissionService  extends BaseService<AuthRolePermission,AuthRolePermissionExample> {

    /**
     * 角色权限
     * @param data 权限数据
     * @param roleId 角色id
     * @return
     */
    @GetMapping("update-permission-for-role")
    int updatePermissionForRole(JSONArray data, String roleId);

}