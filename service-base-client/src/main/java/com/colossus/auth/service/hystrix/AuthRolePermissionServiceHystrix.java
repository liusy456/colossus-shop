package com.colossus.auth.service.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.colossus.auth.service.AuthRolePermissionService;
import com.colossus.common.dao.AuthRolePermissionMapper;
import com.colossus.common.model.AuthRolePermission;
import com.colossus.common.model.AuthRolePermissionExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthRolePermissionServiceHystrix extends BaseServiceHystrix<AuthRolePermissionMapper,AuthRolePermission,AuthRolePermissionExample> implements AuthRolePermissionService {

    @Override
    public int updatePermissionForRole(JSONArray data, String roleId) {
        return 0;
    }
}
