package com.colossus.auth.service;

import com.colossus.auth.service.hystrix.AuthRoleServiceHystrix;
import com.colossus.common.model.AuthRole;
import com.colossus.common.model.AuthRoleExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;

/**
* AuthRoleService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthRoleServiceHystrix.class)
public interface AuthRoleService extends BaseService<AuthRole,AuthRoleExample> {

}