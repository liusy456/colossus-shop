package com.colossus.member.service;

import com.colossus.member.client.vo.UserVo;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:20
 **/
public interface UserService {

    UserVo findUserById(String id);
}
