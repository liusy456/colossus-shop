package com.colossus.common.vo;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class PageResponse<T>  implements Serializable{
    private static final long serialVersionUID = -1337201897211410794L;

    private List<T> content;
    private boolean first;
    private boolean last;
    private int number;
    private int numberOfElements;
    private int size;
    private long totalElements;
    private int totalPages;


   public PageResponse(){

   }

    public PageResponse(Page<T> page) {
        this.content=page.getContent();
        this.first=page.isFirst();
        this.last=page.isLast();
        this.number=page.getNumber();
        this.numberOfElements=page.getNumberOfElements();
        this.size=page.getSize();
        this.totalElements=page.getTotalElements();
        this.totalPages=page.getTotalPages();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
