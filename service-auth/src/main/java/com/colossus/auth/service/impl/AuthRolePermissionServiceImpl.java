package com.colossus.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.colossus.auth.service.AuthRolePermissionService;
import com.colossus.common.dao.AuthRolePermissionMapper;
import com.colossus.common.model.AuthRolePermission;
import com.colossus.common.model.AuthRolePermissionExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RefreshScope
@Api(value = "authRolePermission-service",description = "角色权限服务")
public class AuthRolePermissionServiceImpl  extends BaseServiceImpl<AuthRolePermissionMapper,AuthRolePermission,AuthRolePermissionExample> implements AuthRolePermissionService {

    @Autowired
    private AuthRolePermissionMapper authRolePermissionMapper;
    @Override
    public int updatePermissionForRole(JSONArray data, String roleId) {
        List<String> deleteIds = new ArrayList<>();
        for (int i = 0; i < data.size(); i ++) {
            JSONObject json = data.getJSONObject(i);
            if (!json.getBoolean("checked")) {
                deleteIds.add(json.getString("id"));
            } else {
                // 新增权限
                AuthRolePermission authRolePermission = new AuthRolePermission();
                authRolePermission.setRoleId(roleId);
                authRolePermission.setPermissionId(json.getString("id"));
                authRolePermissionMapper.insertSelective(authRolePermission);
            }
        }
        // 删除权限
        if (deleteIds.size() > 0) {
            AuthRolePermissionExample authRolePermissionExample = new AuthRolePermissionExample();
            authRolePermissionExample.createCriteria()
                    .andPermissionIdIn(deleteIds)
                    .andRoleIdEqualTo(roleId);
            authRolePermissionMapper.deleteByExample(authRolePermissionExample);
        }
        return data.size();
    }
}
