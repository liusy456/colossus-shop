package com.colossus.auth.service.impl;

import com.colossus.auth.service.AuthLogService;
import com.colossus.common.dao.AuthLogMapper;
import com.colossus.common.model.AuthLog;
import com.colossus.common.model.AuthLogExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Api(value = "authLog-service",description = "权限日志服务")
public class AuthLogServiceImpl extends BaseServiceImpl<AuthLogMapper,AuthLog,AuthLogExample> implements AuthLogService {

    @Autowired
    private AuthLogMapper authLogMapper;
    @Override
    public int insertAuthLogSelective(AuthLog authLog) {
        return authLogMapper.insertSelective(authLog);
    }
}
