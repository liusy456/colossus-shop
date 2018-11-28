package com.colossus.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "item",type = "item")
@Data
public class Item {

    @Id
    @Field(type = FieldType.Text,store = true)
    private String id;
    @Field(type = FieldType.Text,store = true)
    private String title;
    @Field(type = FieldType.Text,store = true)
    private String categoryName;
    @Field(type = FieldType.Text,store = true)
    private String imgUrl;
    @Field(type = FieldType.Float,store = true)
    private float price;
    @Field(type = FieldType.Text,store = true)
    private String SellPoint;
    @Field(type = FieldType.Text,store = true)
    private String description;
}
