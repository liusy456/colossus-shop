package com.colossus.member.service;

import com.colossus.member.service.hystrix.AuthOrganizationServiceHystrix;
import com.colossus.common.model.AuthOrganization;
import com.colossus.common.model.AuthOrganizationExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;

/**
* AuthOrganizationService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthOrganizationServiceHystrix.class)
public interface AuthOrganizationService extends BaseService<AuthOrganization,AuthOrganizationExample> {

}