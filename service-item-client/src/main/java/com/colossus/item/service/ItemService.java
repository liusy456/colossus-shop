package com.colossus.item.service;

import com.colossus.common.model.Item;
import com.colossus.common.model.ItemDesc;
import com.colossus.item.service.hystrix.ItemServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 商品 Service
 */

@FeignClient(value = "service-item",fallback = ItemServiceHystrix.class)
public interface ItemService {

    @RequestMapping(value = "/getItemById/{id}",method = RequestMethod.POST)
    Item getItemById(@PathVariable("id") String itemId);

    @RequestMapping(value = "/getItemDescById/{id}",method = RequestMethod.POST)
    ItemDesc getItemDescById(@PathVariable("id") String itemId);


}
