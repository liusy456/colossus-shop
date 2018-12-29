package com.colossus.member.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:22
 **/
@RestController
@RequestMapping("api/user")
public class UserApi {

    @PostMapping("register")
    public void register(){

    }
}
