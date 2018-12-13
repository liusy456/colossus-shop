package com.colossus.search.service;

/**
 * @author Tlsy1
 * @since 2018-12-13 16:13
 **/
public interface SearchService {
    void importAllItems();

    void search( String queryString,
                 Integer page, Integer pageSize);
}
