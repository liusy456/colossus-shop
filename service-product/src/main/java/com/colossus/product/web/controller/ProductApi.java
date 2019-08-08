package com.colossus.product.web.controller;

import com.colossus.product.model.Shop;
import com.colossus.product.model.Sku;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:30
 **/
@RestController
@RequestMapping("/api/v1/product")
public class ProductApi {

    @GetMapping("list-shop")
    public Page<Shop> listShop(){
        return null;
    }

    @GetMapping("list-sku")
    public Page<Sku> listSku(String shopId){
        return null;
    }
}
