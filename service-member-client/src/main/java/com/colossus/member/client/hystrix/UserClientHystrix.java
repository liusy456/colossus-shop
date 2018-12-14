package com.colossus.member.client.hystrix;

import com.colossus.member.client.UserClient;
import com.colossus.member.client.vo.UserVo;

/**
 * @author Tlsy1
 * @since 2018-12-13 10:10
 **/
public class UserClientHystrix implements UserClient {
    @Override
    public UserVo findUserById(String id) {
        return null;
    }
}
