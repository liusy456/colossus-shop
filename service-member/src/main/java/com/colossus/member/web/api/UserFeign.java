package com.colossus.member.web.api;

import com.colossus.member.client.UserClient;
import com.colossus.member.client.vo.UserVo;
import com.colossus.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:22
 **/
@RestController
public class UserFeign implements UserClient {

    @Autowired
    private UserService userService;

    @Override
    public UserVo findUserById(String id) {
        return userService.findUserById(id);
    }
}
