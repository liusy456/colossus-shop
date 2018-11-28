package com.colossus.cart.service;


import com.colossus.cart.service.hystrix.CartServiceHystrix;
import com.colossus.common.model.BaseResult;
import com.colossus.common.model.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 购物车服务
 */

@FeignClient(value = "service-cart", fallback = CartServiceHystrix.class)
public interface CartService {
    @PostMapping(value = "add-cart")
    BaseResult addCart(
            @RequestParam("pid") String pid,
            @RequestParam("pcount") Integer pcount,
            @RequestParam("uuid") String uuid
    );

    @PostMapping(value = "get-cart-info-by-cookies-id")
    List<CartInfo> getCartInfoListByCookiesId(@RequestParam("cookieUUID") String cookieUUID);

    /**
     * 根据商品id和数量对购物车增加商品或减少商品
     *
     * @param pid    商品id
     * @param pcount 增加数量
     * @param type   1 增加 2 减少
     * @param index  商品位置   ps:用于直接定位商品 不用遍历整个购物车
     * @return
     */
    @PostMapping(value = "/decre--or-incre")
    BaseResult decreOrIncre(
            @RequestParam("pid") Long pid,
            @RequestParam("pcount") Integer pcount,
            @RequestParam("type") Integer type,
            @RequestParam("index") Integer index,
            @RequestParam("cookieUUID") String cookieUUID
    );


}
