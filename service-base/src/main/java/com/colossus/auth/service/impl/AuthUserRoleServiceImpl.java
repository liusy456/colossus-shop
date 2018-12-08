package com.colossus.auth.service.impl;

import com.colossus.auth.service.AuthUserRoleService;
import com.colossus.common.dao.AuthUserRoleMapper;
import com.colossus.common.model.AuthUserRole;
import com.colossus.common.model.AuthUserRoleExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Api(value = "authUserRole-service",description = "用户角色服务")
public class AuthUserRoleServiceImpl extends BaseServiceImpl<AuthUserRoleMapper,AuthUserRole,AuthUserRoleExample> implements AuthUserRoleService {

    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;
    @Override
    public int updateRoleForUser(String[] roleIds, String userId) {
        int result = 0;
        // 删除旧记录
        AuthUserRoleExample authUserRoleExample = new AuthUserRoleExample();
        authUserRoleExample.createCriteria()
                .andUserIdEqualTo(userId);
        authUserRoleMapper.deleteByExample(authUserRoleExample);
        // 增加新记录
        if (null != roleIds) {
            for (String roleId : roleIds) {
                if (StringUtils.isBlank(roleId)) {
                    continue;
                }
                AuthUserRole authUserRole = new AuthUserRole();
                authUserRole.setUserId(userId);
                authUserRole.setRoleId(roleId);
                result = authUserRoleMapper.insertSelective(authUserRole);
            }
        }
        return result;
    }
}
