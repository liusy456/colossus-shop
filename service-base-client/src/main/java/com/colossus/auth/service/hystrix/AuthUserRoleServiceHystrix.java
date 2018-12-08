package com.colossus.auth.service.hystrix;

import com.colossus.auth.service.AuthUserRoleService;
import com.colossus.common.dao.AuthUserRoleMapper;
import com.colossus.common.model.AuthUserRole;
import com.colossus.common.model.AuthUserRoleExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthUserRoleServiceHystrix extends BaseServiceHystrix<AuthUserRoleMapper,AuthUserRole,AuthUserRoleExample> implements AuthUserRoleService {


    @Override
    public int updateRoleForUser(String[] roleIds, String userId) {
        return 0;
    }
}
