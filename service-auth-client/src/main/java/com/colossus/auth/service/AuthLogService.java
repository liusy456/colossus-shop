package com.colossus.auth.service;

import com.colossus.auth.service.hystrix.AuthLogServiceHystrix;
import com.colossus.common.model.AuthLog;
import com.colossus.common.model.AuthLogExample;
import com.colossus.common.service.BaseService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
* AuthLogService接口
* Created by Tlsy on 2017/3/20.
*/
@FeignClient(value = "service-auth",fallback = AuthLogServiceHystrix.class)
public interface AuthLogService extends BaseService<AuthLog,AuthLogExample> {

    /**
     * 写入操作日志
     * @param authLog
     * @return
     */
    @PostMapping("insert-authLog")
    int insertAuthLogSelective(AuthLog authLog);
}