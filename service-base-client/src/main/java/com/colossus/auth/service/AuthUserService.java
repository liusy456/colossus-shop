package com.colossus.auth.service;


import com.colossus.auth.service.hystrix.AuthUserServiceHystrix;
import com.colossus.common.model.AuthUser;
import com.colossus.common.model.AuthUserExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
* AuthUserService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthUserServiceHystrix.class)
public interface AuthUserService extends BaseService<AuthUser,AuthUserExample>{

    @PostMapping("create-auth-user")
    AuthUser createUser(AuthUser AuthUser);

}