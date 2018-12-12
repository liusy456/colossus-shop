package com.colossus.member.service.hystrix;

import com.colossus.member.service.AuthOrganizationService;
import com.colossus.common.dao.AuthOrganizationMapper;
import com.colossus.common.model.AuthOrganization;
import com.colossus.common.model.AuthOrganizationExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthOrganizationServiceHystrix extends BaseServiceHystrix<AuthOrganizationMapper,AuthOrganization,AuthOrganizationExample>  implements AuthOrganizationService {
}
