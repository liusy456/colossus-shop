package com.colossus.auth.service;

import com.alibaba.fastjson.JSONArray;
import com.colossus.auth.service.hystrix.AuthPermissionServiceHystrix;
import com.colossus.common.model.AuthPermission;
import com.colossus.common.model.AuthPermissionExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
* AuthPermissionService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthPermissionServiceHystrix.class)
public interface AuthPermissionService extends BaseService<AuthPermission,AuthPermissionExample> {

    @GetMapping("get-tree-by-roleId")
    JSONArray getTreeByRoleId(String  roleId);

    @GetMapping("get-tree-by-userId")
    JSONArray getTreeByUserId(String  userId, Byte type);

}