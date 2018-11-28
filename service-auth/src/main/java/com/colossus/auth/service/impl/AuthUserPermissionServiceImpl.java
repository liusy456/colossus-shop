package com.colossus.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.colossus.auth.service.AuthUserPermissionService;
import com.colossus.common.dao.AuthUserPermissionMapper;
import com.colossus.common.model.AuthUserPermission;
import com.colossus.common.model.AuthUserPermissionExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Api(value = "authUserPermission-service",description = "用户权限服务")
public class AuthUserPermissionServiceImpl  extends BaseServiceImpl<AuthUserPermissionMapper,AuthUserPermission,AuthUserPermissionExample> implements AuthUserPermissionService {

    @Autowired
    private AuthUserPermissionMapper authUserPermissionMapper;
    @Override
    public int updatePermissionForUser(JSONArray data, String userId) {
        for (int i = 0; i < data.size(); i ++) {
            JSONObject json = data.getJSONObject(i);
            if (json.getBoolean("checked")) {
                // 新增权限
                AuthUserPermission authUserPermission = new AuthUserPermission();
                authUserPermission.setUserId(userId);
                authUserPermission.setPermissionId(json.getString("id"));
                authUserPermission.setType(json.getByte("type"));
                authUserPermissionMapper.insertSelective(authUserPermission);
            } else {
                // 删除权限
                AuthUserPermissionExample authUserPermissionExample = new AuthUserPermissionExample();
                authUserPermissionExample.createCriteria()
                        .andPermissionIdEqualTo(json.getString("id"))
                        .andTypeEqualTo(json.getByte("type"));
                authUserPermissionMapper.deleteByExample(authUserPermissionExample);
            }
        }
        return data.size();
    }
}
