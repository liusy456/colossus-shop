package com.colossus.search.web.api;

import com.colossus.search.client.SearchClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tlsy1
 * @since 2018-12-13 16:16
 **/
@RestController
public class SearchFeign implements SearchClient {
    @Override
    public void importAllItems() {

    }

    @Override
    public void search(String queryString, Integer page, Integer rows) {

    }
}
