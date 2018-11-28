package com.colossus.order.service;


import com.colossus.common.model.BaseResult;
import com.colossus.order.service.hystrix.OrderServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-order",fallback = OrderServiceHystrix.class)
public interface OrderService {
    /**
     * 提交订单
     *
     * @param userCookieValue   用户登录Cookie
     * @param cartCookieValue   购物车Cookie
     * @param addrId            用户地址id
     * @param noAnnoyance       运费险
     * @param paymentType       支付方式 1、货到付款，2、在线支付，3、微信支付，4、支付宝支付
     * @param shippingName      快递名称 固定顺丰速运
     * @return
     */
    @RequestMapping(value = "/generateOrder",method = RequestMethod.POST)
    BaseResult generateOrder(
            @RequestParam("userCookieValue")    String userCookieValue,
            @RequestParam("cartCookieValue")    String cartCookieValue,
            @RequestParam("addrId")             String addrId,
            @RequestParam("noAnnoyance")        Integer noAnnoyance,
            @RequestParam("paymentType")        Integer paymentType,
            @RequestParam("orderId")            String orderId,
            @RequestParam("shippingName")       String shippingName
    );
}
