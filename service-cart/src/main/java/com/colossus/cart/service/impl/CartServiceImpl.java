package com.colossus.cart.service.impl;

import com.colossus.cart.service.CartService;
import com.colossus.common.dao.ItemMapper;
import com.colossus.common.model.BaseResult;
import com.colossus.common.model.CartInfo;
import com.colossus.common.model.Item;
import com.colossus.common.model.ItemExample;
import com.colossus.common.utils.FastJsonConvert;
import com.colossus.redis.service.RedisService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车操作实现
 */


//@Transactional
@Api(value = "API - CartServiceImpl", description = "购物车操作")
@RestController
@RefreshScope
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Value("${redisKey.prefix.cart_info_profix}")
    private String CART_INFO_PROFIX;
    @Value("${redisKey.prefix.redis_cart_expire_time}")
    private Integer REDIS_CART_EXPIRE_TIME;
    @Value("${redisKey.prefix.item_info_profix}")
    private String ITEM_INFO_PROFIX;
    @Value("${redisKey.prefix.item_info_base_suffix}")
    private String ITEM_INFO_BASE_SUFFIX;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    @ApiOperation("购物车添加商品")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pid", value = "", required = true, dataType = "Long"),
                    @ApiImplicitParam(name = "pcount", value = "", required = true, dataType = "Long"),
                    @ApiImplicitParam(name = "uuid", value = "", required = true, dataType = "Long"),
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
    public BaseResult addCart(String pid, Integer pcount, String uuid) {

        String key = CART_INFO_PROFIX + uuid;
        String cartInfoString = null;
        try {
            cartInfoString = redisService.get(key);
        } catch (Exception e) {
            logger.error("Redis出错!", e);
        }

        Item item = null;

        String redisItem = null;
        try {
            redisItem = redisService.get(ITEM_INFO_PROFIX + pid + ITEM_INFO_BASE_SUFFIX);
        } catch (Exception e) {
            logger.error("Redis error", e);
        }

        if (StringUtils.isNotBlank(redisItem)) {
            item = FastJsonConvert.convertJSONToObject(redisItem, Item.class);

        } else {
            ItemExample example = new ItemExample();
            ItemExample.Criteria criteria = example.createCriteria();

            criteria.andIdEqualTo(pid);

            List<Item> itemList = null;

            try {
                itemList = itemMapper.selectByExample(example);
            } catch (Exception e) {
                logger.error("select DB error", e);
            }

            if (itemList != null && itemList.size() > 0) {
                item = itemList.get(0);
            } else {
                return BaseResult.build(500, "商品查询不到!");
            }
        }

        CartInfo cartInfo = new CartInfo();

        cartInfo.setId(item.getId());
        cartInfo.setName(item.getTitle());
        String[] split = item.getImage().split(",");
        cartInfo.setImageUrl(split[0]);
        cartInfo.setColour("黑色");
        cartInfo.setNum(pcount);
        cartInfo.setPrice(item.getPrice());
        cartInfo.setSize("32GB");

        if (StringUtils.isBlank(cartInfoString)) {

            ArrayList<CartInfo> cartInfos = new ArrayList<>();

            cartInfos.add(cartInfo);

            logger.debug("第一次保存商品到Redis uuid:" + uuid);

            try {
                redisService.set(key, FastJsonConvert.convertObjectToJSON(cartInfos));
                redisService.expire(key, REDIS_CART_EXPIRE_TIME);
            } catch (Exception e) {
                logger.error("Redis error", e);
            }

            return BaseResult.build(200, "ok", cartInfo);

        } else {
            List<CartInfo> list = FastJsonConvert.convertJSONToArray(cartInfoString, CartInfo.class);
            if (list != null && list.size() > 0) {
                boolean flag = true;
                for (int i = 0; i < list.size(); i++) {
                    CartInfo Info = list.get(i);
                    if (Info.getId().equals(item.getId())) {
                        Info.setNum(Info.getNum() + pcount);
                        list.remove(i);
                        list.add(Info);
                        flag = false;

                        logger.debug("商品已经存在数量加" + pcount + "个 uuid:" + uuid);
                    }
                }
                if (flag) {
                    list.add(cartInfo);
                    logger.debug("商品不存在数量为" + pcount + "个 uuid:" + uuid);
                }
            }

            logger.debug("商品添加完成 购物车" + list.size() + "件商品 uuid:" + uuid);

            try {
                redisService.set(key, FastJsonConvert.convertObjectToJSON(list));
                redisService.expire(key, REDIS_CART_EXPIRE_TIME);
            } catch (Exception e) {
                logger.error("Redis出错!", e);
            }

            return BaseResult.build(200, "ok", cartInfo);
        }
    }

    @Override
    @ApiOperation("获取商品信息")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "cookieUUID", value = "", required = true, dataType = "Long"),
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
    public List<CartInfo> getCartInfoListByCookiesId(String cookieUUID) {

        String cartInfoString = redisService.get(CART_INFO_PROFIX + cookieUUID);

        if (StringUtils.isNotBlank(cartInfoString)) {
            List<CartInfo> cartInfos = FastJsonConvert.convertJSONToArray(cartInfoString, CartInfo.class);

            return cartInfos;
        }

        return null;
    }

    /**
     *
     * 根据商品id和数量对购物车增加商品或减少商品
     *
     * @param pid       商品id
     * @param pcount    增加数量
     * @param type      1 增加 2 减少
     * @param index     商品位置   ps:用于直接定位商品 不用遍历整个购物车
     * @return
     */
    @Override
    @ApiOperation("获取商品信息")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pid", value = "", required = true, dataType = "Long"),
                    @ApiImplicitParam(name = "pcount", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "type", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "index", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "cookieUUID", value = "", required = true, dataType = "String"),
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
    public BaseResult decreOrIncre(Long pid, Integer pcount, Integer type, Integer index, String cookieUUID) {

        String key = CART_INFO_PROFIX + cookieUUID;

        List<CartInfo> cartInfoList = getCartInfoListByCookiesId(cookieUUID);
        if (cartInfoList == null || cartInfoList.size() == 0) {
            return BaseResult.build(400, "购物车没有此商品 请不要非法操作!");
        }

        CartInfo cartInfo = cartInfoList.get(index);

        if (type == 1) {
            cartInfo.setNum(cartInfo.getNum() + pcount);
        } else {
            cartInfo.setNum(cartInfo.getNum() - pcount);
        }
        //cartInfoList.remove(index);
        //cartInfoList.add(index, cartInfo);
        redisService.set(key, FastJsonConvert.convertObjectToJSON(cartInfoList));
        redisService.expire(key,REDIS_CART_EXPIRE_TIME);

        return BaseResult.ok();
    }


}
