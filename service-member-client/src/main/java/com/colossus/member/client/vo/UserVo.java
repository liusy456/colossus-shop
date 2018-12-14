package com.colossus.member.client.vo;

import lombok.Data;

/**
 * @author Tlsy1
 * @since 2018-12-14 11:51
 **/
@Data
public class UserVo {

    private String id;

    private String phone;

    private String email;

    private String avatar;

    private String name;

    private String defaultShippingAddress;

}
