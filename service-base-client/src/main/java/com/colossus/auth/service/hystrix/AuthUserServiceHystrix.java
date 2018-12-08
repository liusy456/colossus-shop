package com.colossus.auth.service.hystrix;

import com.colossus.auth.service.AuthUserService;
import com.colossus.common.dao.AuthUserMapper;
import com.colossus.common.model.AuthUser;
import com.colossus.common.model.AuthUserExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthUserServiceHystrix extends BaseServiceHystrix<AuthUserMapper,AuthUser,AuthUserExample> implements AuthUserService {
    @Override
    public AuthUser createUser(AuthUser AuthUser) {
        return null;
    }
}
