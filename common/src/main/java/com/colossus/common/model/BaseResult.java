package com.colossus.common.model;

/**
 * 自定义响应结构
 */
public class BaseResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public static BaseResult build(Integer status, String msg, Object data) {
        return new BaseResult(status, msg, data);
    }

    public static BaseResult ok(Object data) {
        return new BaseResult(data);
    }

    public static BaseResult ok() {
        return new BaseResult(null);
    }

    public BaseResult() {

    }

    public static BaseResult build(Integer status, String msg) {
        return new BaseResult(status, msg, null);
    }

    public BaseResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public BaseResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



}
