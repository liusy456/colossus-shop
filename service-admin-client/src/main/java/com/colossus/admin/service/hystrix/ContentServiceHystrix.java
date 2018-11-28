package com.colossus.admin.service.hystrix;

import com.colossus.admin.service.ContentService;
import com.colossus.common.model.BaseResult;
import com.colossus.common.model.CategorySecondary;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 内容维护 熔断处理
 */

@Component
public class ContentServiceHystrix implements ContentService {


    @Override
    public Map<String, Object> getCategoryList(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength) {
        return null;
    }

    @Override
    public BaseResult saveCategory(String id, String name, Integer sort_order) {
        return null;
    }

    @Override
    public Map<String, Object> getCategorySecondaryList(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength) {
        return null;
    }

    @Override
    public Map<String, Object> getSearchCategorySecondaryList(String sSearch, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength) {
        return null;
    }

    @Override
    public BaseResult saveCategorySecondary(CategorySecondary categorySecondary) {
        return null;
    }
}
