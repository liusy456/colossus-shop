package com.colossus.common.code_enum;

/**
 * @author Tlsy
 * @version oldriver-all 0.0.1
 * @date 2017/4/18  18:10
 */
public enum RequestMethodEnum {
    POST("post"),
    GET("get"),
    PUT("put"),
    DELETE("delete");
    private String method;
    RequestMethodEnum(String method){
        this.method=method;
    }

    public String value(){
        return this.method;
    }
}