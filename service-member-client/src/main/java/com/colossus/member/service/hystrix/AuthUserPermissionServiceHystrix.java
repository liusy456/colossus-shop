package com.colossus.member.service.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.colossus.member.service.AuthUserPermissionService;
import com.colossus.common.dao.AuthUserPermissionMapper;
import com.colossus.common.model.AuthUserPermission;
import com.colossus.common.model.AuthUserPermissionExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthUserPermissionServiceHystrix extends BaseServiceHystrix<AuthUserPermissionMapper,AuthUserPermission,AuthUserPermissionExample> implements AuthUserPermissionService {

    @Override
    public int updatePermissionForUser(JSONArray datas, String userId) {
        return 0;
    }
}
