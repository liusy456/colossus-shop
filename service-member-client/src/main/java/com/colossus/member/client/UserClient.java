package com.colossus.member.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Tlsy1
 * @since 2018-12-13 10:02
 **/
@FeignClient(value = "member",path = "/member")
public interface UserClient {
}
