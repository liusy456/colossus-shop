package com.colossus.member.service.impl;

import com.colossus.member.client.vo.UserVo;
import com.colossus.member.dao.UserMapper;
import com.colossus.member.model.User;
import com.colossus.member.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:21
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVo findUserById(String id) {
        UserVo userVo = new UserVo();
        User user = userMapper.selectByPrimaryKey(id);
        if(user==null){
            return null;
        }
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
}
