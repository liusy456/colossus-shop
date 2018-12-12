package com.colossus.member.service.hystrix;

import com.colossus.member.service.AuthLogService;
import com.colossus.common.dao.AuthLogMapper;
import com.colossus.common.model.AuthLog;
import com.colossus.common.model.AuthLogExample;
import com.colossus.common.service.hystrix.BaseServiceHystrix;
import org.springframework.stereotype.Component;

@Component
public class AuthLogServiceHystrix extends BaseServiceHystrix<AuthLogMapper,AuthLog,AuthLogExample> implements AuthLogService {
    @Override
    public int insertAuthLogSelective(AuthLog authLog) {
        return 0;
    }
}
