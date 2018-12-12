package com.colossus.member.service;

import com.colossus.member.service.hystrix.AuthSystemServiceHystrix;
import com.colossus.common.model.AuthSystem;
import com.colossus.common.model.AuthSystemExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;

/**
* AuthSystemService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthSystemServiceHystrix.class)
public interface AuthSystemService extends BaseService<AuthSystem,AuthSystemExample> {

}