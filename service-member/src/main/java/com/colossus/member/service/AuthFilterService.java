package com.colossus.member.service;

import com.colossus.member.client.vo.AuthFilterVo;

import java.util.List;

/**
 * @author Tlsy1
 * @since 2018-12-14 14:24
 **/
public interface AuthFilterService {

    List<AuthFilterVo> findAuthFilterByServiceId(String serviceId);

}
