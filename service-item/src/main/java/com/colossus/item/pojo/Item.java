package com.colossus.item.pojo;

import org.apache.commons.lang3.StringUtils;


/**
 * Item 扩展pojo
 */


public class Item extends com.colossus.common.model.Item {

    private String[] images;

    public Item(com.colossus.common.model.Item item) {
        //初始化属性
        this.setId(item.getId());
        this.setTitle(item.getTitle());
        this.setSellPoint(item.getSellPoint());
        this.setPrice(item.getPrice());
        this.setNum(item.getNum());
        this.setBarcode(item.getBarcode());
        this.setImage(item.getImage());
        this.setCid(item.getCid());
        this.setStatus(item.getStatus());
        this.setCreateTime(item.getCreateTime());
        this.setUpdateTime(item.getUpdateTime());
    }

    public String[] getImages() {

        if (StringUtils.isNotBlank(this.getImage())) {

            String[] split = this.getImage().split(",");

            return split;
        } else {
            return null;
        }

    }

}
