package com.colossus.search.mapper;

import com.colossus.search.model.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ItemMapper extends ElasticsearchRepository<Item,String> {
}
