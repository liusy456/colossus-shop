package com.colossus.portal.service.impl;


import com.colossus.common.dao.ContentMapper;
import com.colossus.common.model.Content;
import com.colossus.common.model.ContentExample;
import com.colossus.common.utils.FastJsonConvert;
import com.colossus.portal.service.PortalContentService;
import com.colossus.redis.service.RedisService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页内容Service
 * @create 2017-05-04
 */
@Api(value = "API - PortalContentServiceImpl", description = "首页操作")
@RestController
@RefreshScope
public class PortalContentServiceImpl implements PortalContentService {

    private static final Logger logger = LoggerFactory.getLogger(PortalContentServiceImpl.class);

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisService redisService;

    @Value("${redisKey.prefix.index_ad}")
    private String INDEX_AD;


    @Override
    @ApiOperation("获取首页广告")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "bigAdIndex", value = "", required = true, dataType = "Long"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public List<Content> getContentByCid(String bigAdIndex) {

        //先查询缓存

        try {
            logger.info("=====>查询Redis");
            String list = redisService.hget(INDEX_AD, bigAdIndex + "");

            if (StringUtils.isNotBlank(list)) {

                logger.info("=======>查询Redis 返回结果");
                return FastJsonConvert.convertJSONToArray(list, Content.class);
            }

        } catch (Exception e) {
            logger.error("{}",e);
        }


        ContentExample example = new ContentExample();
        ContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(bigAdIndex);

        logger.info("=======>查询数据库");
        List<Content> list = contentMapper.selectByExample(example);

        //添加缓存
        try {
            logger.info("=======>添加缓存");
            redisService.hset(INDEX_AD, bigAdIndex + "", FastJsonConvert.convertObjectToJSON(list));
        } catch (Exception e) {
            logger.error("{}",e);
        }

        return list;

    }

}
