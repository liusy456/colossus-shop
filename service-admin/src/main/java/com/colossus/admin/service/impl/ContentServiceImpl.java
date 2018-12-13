package com.colossus.admin.service.impl;


import com.colossus.admin.service.ContentService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 内容维护 ServiceImpl
 */

@Api(value = "API - ContentServiceImpl", description = "内容操作")
@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

}
