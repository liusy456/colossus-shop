package com.colossus.search.client.hystrix;

import com.colossus.search.client.SearchClient;
import org.springframework.stereotype.Component;

/**
 * 搜索服务 熔断处理
 */

@Component
public class SearchClientHystrix implements SearchClient {


    /**
     * 导入全部商品索引
     *
     * @return
     */
    @Override
    public void importAllItems() {

    }

    /**
     * 查询商品
     *
     * @param queryString 查询条件
     * @param page        第几页
     * @param rows        每页几条
     * @return 返回商品Json
     * @throws Exception
     */
    @Override
    public void search(String queryString, Integer page, Integer rows) {

    }
}
