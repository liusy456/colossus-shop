package com.colossus.auth.service.hystrix;

import com.colossus.auth.service.AuthRoleService;
import com.colossus.common.dao.AuthRoleMapper;
import com.colossus.common.model.AuthRole;
import com.colossus.common.model.AuthRoleExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthRoleServiceHystrix extends BaseServiceHystrix<AuthRoleMapper,AuthRole,AuthRoleExample> implements AuthRoleService {
}
