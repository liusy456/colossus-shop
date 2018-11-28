package com.colossus.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseModel implements Serializable{
    private static final long serialVersionUID = -2837666881999845745L;

    private String id;
    private Date createTime;
    private Date updateTime;
}
