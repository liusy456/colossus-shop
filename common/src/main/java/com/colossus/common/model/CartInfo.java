package com.colossus.common.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Data
public class CartInfo extends BaseModel {
    private static final long serialVersionUID = 5314476076440589573L;
    private String name;
    private String imageUrl;
    private String colour;
    private String size;
    private Long price;
    private Long weight;
    private Integer num;
    @Setter(value = AccessLevel.PRIVATE)
    private Long sum;

    public Long getSum() {
        return price * num;
    }

}
