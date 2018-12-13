package com.colossus.search.client;


import com.colossus.search.client.hystrix.SearchClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * es Service
 */

@FeignClient(value = "service-search",fallback = SearchClientHystrix.class)
public interface SearchClient {

    /**
     * 导入全部商品索引
     *
     * @return
     */
    @RequestMapping(value = "/importAllItems",method = RequestMethod.POST)
    void importAllItems();

    /**
     * 查询商品
     * @param queryString 查询条件
     * @param page 第几页
     * @param rows 每页几条
     * @return 返回商品Json
     * @throws Exception
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    void search(
            @RequestParam("q")                  String queryString,
            @RequestParam(name = "page",defaultValue = "1")   Integer page,
            @RequestParam(name = "rows",defaultValue = "0")   Integer rows
    );
}
