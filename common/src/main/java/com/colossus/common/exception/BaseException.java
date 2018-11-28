/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/details/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package com.colossus.common.exception;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 业务异常基类，所有业务异常都必须继承于此异常 
 * 
 */
public class BaseException extends RuntimeException {

    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected int code;

    public BaseException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    /**
     * 实例化异常
     * 
     * @param msgFormat
     * @param args
     * @return
     */
    public BaseException newInstance(String msgFormat, Object... args) {
        return new BaseException(this.code, msgFormat, args);
    }

    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable e) {
        if (e == null){
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起.
     */
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 在request中获取异常类
     * @param request
     * @return
     */
    public static Throwable getThrowable(HttpServletRequest request){
        Throwable ex = null;
        if (request.getAttribute("exception") != null) {
            ex = (Throwable) request.getAttribute("exception");
        } else if (request.getAttribute("javax.servlet.error.exception") != null) {
            ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
        return ex;
    }

}
