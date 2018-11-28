package com.colossus.portal.service.hystrix;

import com.colossus.common.model.Content;
import com.colossus.portal.service.PortalContentService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 首页 熔断处理
 *
 */

@Component
public class PortalContentServiceHystrix implements PortalContentService {


    @Override
    public List<Content> getContentByCid(String bigAdIndex) {
        return null;
    }
}
