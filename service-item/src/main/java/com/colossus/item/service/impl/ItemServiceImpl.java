package com.colossus.item.service.impl;

import com.colossus.common.dao.ItemDescMapper;
import com.colossus.common.dao.ItemMapper;
import com.colossus.common.model.Item;
import com.colossus.common.model.ItemDesc;
import com.colossus.common.utils.FastJsonConvert;
import com.colossus.item.service.ItemService;
import com.colossus.redis.service.RedisService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商品 Service 实现
 */
@Api(value = "API - PortalContentServiceImpl", description = "首页操作")
@RestController
@RefreshScope
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;
    
    @Autowired
    private RedisService redisService;

    @Value("${redisKey.prefix.item_info_profix}")
    private String ITEM_INFO_PROFIX;

    @Value("${redisKey.suffix.item_info_base_suffix}")
    private String  ITEM_INFO_BASE_SUFFIX;

    @Value("${redisKey.suffix.item_info_desc_suffix}")
    private String ITEM_INFO_DESC_SUFFIX;

    @Value("${redisKey.expire_time}")
    private Integer REDIS_EXPIRE_TIME;

    @Override
    @ApiOperation("获取商品信息")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "itemId", value = "", required = true, dataType = "String"),
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
    public Item getItemById(@PathVariable("id") String itemId) {

        String key = ITEM_INFO_PROFIX + itemId + ITEM_INFO_BASE_SUFFIX;

        try {
            String jsonItem = redisService.get(key);

            if (StringUtils.isNotBlank(jsonItem)) {

                logger.info("Redis 查询 商品信息 商品ID:" + itemId);

                return FastJsonConvert.convertJSONToObject(jsonItem, Item.class);

            } else {
                logger.error("Redis 查询不到 key:" + key);
            }
        } catch (Exception e) {
            logger.error("商品信息 获取缓存报错",e);
        }

        logger.info("根据商品ID"+itemId+"查询商品！");
        Item item = itemMapper.selectByPrimaryKey(itemId);

        try {
            redisService.set(key, FastJsonConvert.convertObjectToJSON(item));

            redisService.expire(key, REDIS_EXPIRE_TIME);

            logger.info("Redis 缓存商品信息 key:" + key);

        } catch (Exception e) {
            logger.error("缓存错误商品ID:" + itemId, e);
        }

        return item;

    }

    @Override
    @ApiOperation("获取商品描述")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "itemId", value = "", required = true, dataType = "String"),
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
    public ItemDesc getItemDescById(@PathVariable("id") String itemId) {

        String key = ITEM_INFO_PROFIX + itemId + ITEM_INFO_DESC_SUFFIX;

        try {
            String jsonItem = redisService.get(key);

            if (StringUtils.isNotBlank(jsonItem)) {

                logger.info("Redis query item ID: {}" , itemId);

                return FastJsonConvert.convertJSONToObject(jsonItem, ItemDesc.class);

            } else {
                logger.error("Redis query fail key: {}" ,key);
            }
        } catch (Exception e) {
            logger.error("Redis error", e);
        }
        ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            redisService.set(key, FastJsonConvert.convertObjectToJSON(itemDesc));

            redisService.expire(key, REDIS_EXPIRE_TIME);

            logger.info("Redis query fail key: {}" ,key);

        } catch (Exception e) {
            logger.error("Redis error", e);
        }
        return itemDesc;
    }
}
