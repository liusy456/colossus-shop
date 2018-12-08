package com.colossus.common.vo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by liukx on 17.12.7 0007.
 */
public class PageRequest {

    private int page = 0;
    private int pageSize = 15;
    private String sortBy;
    private Sort.Direction direction = Sort.Direction.DESC;

    public PageRequest() {
    }

    public PageRequest(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public Pageable getPageable(){
        return new org.springframework.data.domain.PageRequest(page,pageSize);
    }

    public Pageable getPageable(String... sorts){
        return new org.springframework.data.domain.PageRequest(page,pageSize,new Sort(Sort.Direction.DESC,sorts));
    }

    public Pageable getPageable(Sort sort){
        return new org.springframework.data.domain.PageRequest(page,pageSize,sort);
    }
}
