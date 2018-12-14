package com.colossus.member.client.hystrix;

import com.colossus.member.client.AuthFilterClient;
import com.colossus.member.client.vo.AuthFilterVo;

import java.util.List;

/**
 * @author Tlsy1
 * @since 2018-12-13 10:10
 **/
public class AuthFilterHystrix implements AuthFilterClient {
    @Override
    public List<AuthFilterVo> findAuthFilterByServiceId(String serviceId) {
        return null;
    }
}
