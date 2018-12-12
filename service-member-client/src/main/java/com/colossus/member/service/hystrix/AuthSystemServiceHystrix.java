package com.colossus.member.service.hystrix;

import com.colossus.member.service.AuthSystemService;
import com.colossus.common.dao.AuthSystemMapper;
import com.colossus.common.model.AuthSystem;
import com.colossus.common.model.AuthSystemExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthSystemServiceHystrix extends BaseServiceHystrix<AuthSystemMapper,AuthSystem,AuthSystemExample> implements AuthSystemService {
}
