package com.colossus.member.client;

import com.colossus.member.client.hystrix.UserClientHystrix;
import com.colossus.member.client.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Tlsy1
 * @since 2018-12-13 10:02
 **/
@FeignClient(value = "member",path = "/member",fallback = UserClientHystrix.class)
public interface UserClient {

    @GetMapping("find-user-by-id")
    UserVo findUserById(@RequestParam(value = "id") String id);
}
