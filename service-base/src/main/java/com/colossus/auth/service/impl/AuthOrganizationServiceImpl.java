package com.colossus.auth.service.impl;

import com.colossus.common.dao.AuthOrganizationMapper;
import com.colossus.common.model.AuthOrganization;
import com.colossus.common.model.AuthOrganizationExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import com.colossus.auth.service.AuthOrganizationService;
import io.swagger.annotations.Api;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Api(value = "authOrganization-service",description = "权限组织服务")
public class AuthOrganizationServiceImpl extends BaseServiceImpl<AuthOrganizationMapper,AuthOrganization,AuthOrganizationExample> implements AuthOrganizationService{
}
