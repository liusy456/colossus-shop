package com.colossus.auth.service;

import com.colossus.auth.service.hystrix.AuthUserOrganizationServiceHystrix;
import com.colossus.common.model.AuthUserOrganization;
import com.colossus.common.model.AuthUserOrganizationExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
* AuthUserOrganizationService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthUserOrganizationServiceHystrix.class)
public interface AuthUserOrganizationService extends BaseService<AuthUserOrganization,AuthUserOrganizationExample> {

    /**
     * 用户组织
     * @param organizationIds 组织ids
     * @param userId 用户id
     * @return
     */
    @PostMapping("update-organization-for-user")
    int updateOrganizationForUser(String[] organizationIds, String userId);

}