package com.colossus.auth.service.hystrix;

import com.colossus.auth.service.AuthUserOrganizationService;
import com.colossus.common.dao.AuthUserOrganizationMapper;
import com.colossus.common.model.AuthUserOrganization;
import com.colossus.common.model.AuthUserOrganizationExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthUserOrganizationServiceHystrix extends BaseServiceHystrix<AuthUserOrganizationMapper,AuthUserOrganization,AuthUserOrganizationExample> implements AuthUserOrganizationService {

    @Override
    public int updateOrganizationForUser(String[] organizationIds, String userId) {
        return 0;
    }
}
