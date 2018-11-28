package com.colossus.auth.service.impl;

import com.colossus.auth.service.AuthUserOrganizationService;
import com.colossus.common.dao.AuthUserOrganizationMapper;
import com.colossus.common.model.AuthUserOrganization;
import com.colossus.common.model.AuthUserOrganizationExample;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@Api(value = "authUserOrganization-service",description = "用户组织服务")
public class AuthUserOrganizationServiceImpl extends BaseServiceImpl<AuthUserOrganizationMapper,AuthUserOrganization,AuthUserOrganizationExample> implements AuthUserOrganizationService{

    @Autowired
    private AuthUserOrganizationMapper authUserOrganizationMapper;
    @Override
    public int updateOrganizationForUser(String[] organizationIds, String userId) {
        int result = 0;
        // 删除旧记录
        AuthUserOrganizationExample authUserOrganizationExample = new AuthUserOrganizationExample();
        authUserOrganizationExample.createCriteria()
                .andUserIdEqualTo(userId);
        authUserOrganizationMapper.deleteByExample(authUserOrganizationExample);
        // 增加新记录
        if (null != organizationIds) {
            for (String organizationId : organizationIds) {
                if (StringUtils.isBlank(organizationId)) {
                    continue;
                }
                AuthUserOrganization authUserOrganization = new AuthUserOrganization();
                authUserOrganization.setUserId(userId);
                authUserOrganization.setOrganizationId(organizationId);
                result =+ authUserOrganizationMapper.insertSelective(authUserOrganization);
            }
        }
        return result;
    }
}
