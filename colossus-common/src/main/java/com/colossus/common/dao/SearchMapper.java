package com.colossus.common.dao;

import com.colossus.common.model.SearchItem;

import java.util.List;

/**
 * Solr操作Mapper
 *
 * @author xubin.
 * @create 2017-02-04 下午4:46
 */
public interface SearchMapper {

    List<SearchItem> getSolrItemList();

    SearchItem getSolrItemByItemId(long itemid);

}
