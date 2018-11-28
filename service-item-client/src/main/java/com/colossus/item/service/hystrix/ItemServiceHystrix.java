package com.colossus.item.service.hystrix;

import com.colossus.common.model.Item;
import com.colossus.common.model.ItemDesc;
import com.colossus.item.service.ItemService;
import org.springframework.stereotype.Component;

/**
 * 商品服务 熔断处理
 *
 */
@Component
public class ItemServiceHystrix implements ItemService {

    @Override
    public Item getItemById(String itemId) {
        System.out.println("hahahhahahah=======================================");
        return null;
    }

    @Override
    public ItemDesc getItemDescById(String itemId) {
        return null;
    }
}
