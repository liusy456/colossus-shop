package com.colossus.auth.service.impl;

import com.colossus.common.dao.AuthSystemMapper;
import com.colossus.common.model.AuthSystem;
import com.colossus.common.model.AuthSystemExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import com.colossus.auth.service.AuthSystemService;
import io.swagger.annotations.Api;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Api(value = "authSystem-service",description = "权限系统服务")
public class AuthSystemServiceImpl extends BaseServiceImpl<AuthSystemMapper,AuthSystem,AuthSystemExample> implements AuthSystemService {
}
