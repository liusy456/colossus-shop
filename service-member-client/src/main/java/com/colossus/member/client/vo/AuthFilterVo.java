package com.colossus.member.client.vo;

import lombok.Data;

/**
 * @author Tlsy1
 * @since 2018-12-14 11:50
 **/
@Data
public class AuthFilterVo {

    private String id;

    private String url;

    private String filter;

    private Integer serviceId;
}
