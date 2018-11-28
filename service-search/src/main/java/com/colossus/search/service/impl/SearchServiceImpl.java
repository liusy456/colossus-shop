package com.colossus.search.service.impl;

import com.colossus.common.dao.SearchMapper;
import com.colossus.common.model.BaseResult;
import com.colossus.common.model.SearchItem;
import com.colossus.common.model.SearchResult;
import com.colossus.search.mapper.ItemMapper;
import com.colossus.search.model.Item;
import com.colossus.search.service.SearchService;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Service 实现类
 *
 */

@RestController
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SearchMapper searchMapper;

    private static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Override
    @ApiOperation("初始化solr数据 导入全部商品数据")
    @ApiResponses({@ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public BaseResult importAllItems() {


        List<SearchItem> searchItemList = searchMapper.getSolrItemList();

        try {
            for (SearchItem searchItem : searchItemList) {

                Item item = new Item();
                item.setId(searchItem.getId());
                item.setCategoryName(searchItem.getCategory_name());
                item.setPrice(searchItem.getPrice());
                item.setDescription(searchItem.getItem_desc());

                String image = searchItem.getImage();
                String[] split = image.split(",");
                item.setImgUrl(split[0]);
                item.setTitle(searchItem.getTitle());
                item.setSellPoint(searchItem.getSell_point());

                itemMapper.save(item);

            }

            logger.info("import success num {}", searchItemList.size());
        } catch (Exception e) {
            logger.error("import error", e);
        }

        return BaseResult.ok();
    }

    @Override
    @ApiOperation("搜索商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "queryString", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "page", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "rows", value = "", required = true, dataType = "Integer")})
    @ApiResponses({@ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public SearchResult search(@RequestParam("q") String queryString,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "0") Integer pageSize) {

        SearchResult searchResult = new SearchResult();

        Iterable<Item> items = itemMapper.findAll(new PageRequest(page - 1, pageSize));
        List<SearchItem> searchItems = Lists.newArrayList();
        for (Item item : items) {
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name(item.getCategoryName());
            searchItem.setId(item.getId());
            searchItem.setImage(item.getImgUrl());
            searchItem.setItem_desc(item.getDescription());
            searchItem.setPrice((long) item.getPrice());
            searchItem.setSell_point(item.getSellPoint());
            searchItem.setTitle(item.getTitle());
            searchItems.add(searchItem);
        }

        searchResult.setItemList(searchItems);
        searchResult.setCurPage(page);

        long recordCount = searchResult.getRecordCount();
        long pageCount = recordCount / pageSize;

        if (recordCount % pageSize > 0) {
            pageCount++;
        }

        searchResult.setPageCount(pageCount);

        return searchResult;

    }
}
