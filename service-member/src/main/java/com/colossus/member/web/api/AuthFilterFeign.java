package com.colossus.member.web.api;

import com.colossus.member.client.AuthFilterClient;
import com.colossus.member.client.vo.AuthFilterVo;
import com.colossus.member.service.AuthFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:23
 **/
@RestController
public class AuthFilterFeign implements AuthFilterClient {

    @Autowired
    private AuthFilterService authFilterService;

    @Override
    public List<AuthFilterVo> findAuthFilterByServiceId(String serviceId) {
        return authFilterService.findAuthFilterByServiceId(serviceId);
    }
}
