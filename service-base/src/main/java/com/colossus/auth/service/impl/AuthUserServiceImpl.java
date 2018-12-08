package com.colossus.auth.service.impl;

import com.colossus.auth.service.AuthUserService;
import com.colossus.common.dao.AuthUserMapper;
import com.colossus.common.model.AuthUser;
import com.colossus.common.model.AuthUserExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Api(value = "authUser-service",description = "用户服务")
public class AuthUserServiceImpl extends BaseServiceImpl<AuthUserMapper,AuthUser,AuthUserExample> implements AuthUserService {

    @Autowired
    private AuthUserMapper authUserMapper;
    @Override
    public AuthUser createUser(AuthUser authUser) {
       AuthUserExample example=new AuthUserExample();
       example.createCriteria().andUsernameEqualTo(authUser.getUsername());

        long count = authUserMapper.countByExample(example);
        if (count > 0) {
            return null;
        }
        authUserMapper.insert(authUser);
        return authUser;

    }
}
