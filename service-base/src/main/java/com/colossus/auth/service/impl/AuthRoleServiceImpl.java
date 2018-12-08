package com.colossus.auth.service.impl;

import com.colossus.common.dao.AuthRoleMapper;
import com.colossus.common.model.AuthRole;
import com.colossus.common.model.AuthRoleExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import com.colossus.auth.service.AuthRoleService;
import io.swagger.annotations.Api;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@Api(value = "authRole-service",description = "用户角色服务")
public class AuthRoleServiceImpl extends BaseServiceImpl<AuthRoleMapper,AuthRole,AuthRoleExample> implements AuthRoleService {
}
