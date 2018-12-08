package com.colossus.auth.service.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.colossus.auth.service.AuthPermissionService;
import com.colossus.common.dao.AuthPermissionMapper;
import com.colossus.common.model.AuthPermission;
import com.colossus.common.model.AuthPermissionExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthPermissionServiceHystrix extends BaseServiceHystrix<AuthPermissionMapper,AuthPermission,AuthPermissionExample> implements AuthPermissionService {
    @Override
    public JSONArray getTreeByRoleId(String roleId) {
        return null;
    }

    @Override
    public JSONArray getTreeByUserId(String userId, Byte type) {
        return null;
    }
}
