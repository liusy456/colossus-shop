package com.colossus.member.service.impl;

import com.colossus.member.client.vo.AuthFilterVo;
import com.colossus.member.dao.AuthFilterMapper;
import com.colossus.member.model.AuthFilter;
import com.colossus.member.service.AuthFilterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tlsy1
 * @since 2018-12-14 14:25
 **/
@Service
public class AuthFilterServiceImpl implements AuthFilterService {

    @Autowired
    private AuthFilterMapper authFilterMapper;

    @Override
    public List<AuthFilterVo> findAuthFilterByServiceId(String serviceId) {
        List<AuthFilter> authFilters = authFilterMapper.selectByServiceId(serviceId);
        return authFilters.stream().map(authFilter -> {AuthFilterVo authFilterVo = new AuthFilterVo();
            BeanUtils.copyProperties(authFilter,authFilterVo);
            return authFilterVo;
        }).collect(Collectors.toList());
    }
}
