package com.colossus.member.client;

import com.colossus.member.client.hystrix.AuthFilterHystrix;
import com.colossus.member.client.vo.AuthFilterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Tlsy1
 * @since 2018-12-13 10:01
 **/
@FeignClient(value = "member",path = "/member",fallback = AuthFilterHystrix.class)
public interface AuthFilterClient {

    @GetMapping("find-auth-filter-by-service-id")
    List<AuthFilterVo> findAuthFilterByServiceId(@RequestParam(value = "serviceId") String serviceId);
}
