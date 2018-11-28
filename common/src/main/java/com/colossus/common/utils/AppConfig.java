package com.colossus.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class AppConfig {

    /**
     * <B>构造方法</B><BR>
     */
    private AppConfig() {
    }

    /**
     * 用户登录后将基本信息放入session中
     */
    public static String USER_SESSION = "user";
    /**
     * 密码加密配置
     */
    public static final int SALT_SIZE=8;

    public static final int HASH_INTERATIONS=1024;

    public static final String HASH_ALGORITHM="SHA-1";

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();
    /**
     * 缓存配置
     */
    public static final String USER_CACHE="userCache";

    public static final String USER_CACHE_ID_="user_cache_id_";

    public static final String USER_CACHE_PHONE_="user_cache_phone_";

    public static final String USER_CACHE_NAME_="user_cache_name_";

    public static final String AUTH_TOKEN_CACHE="authTokenCache";

    public static final String SYS_CACHE="sysCache";

    public static final String PRODUCT_CACHE="productCache";

    public static final String PASSWORD_RETRY_CACHE="passwordRetryCache";
    /** 判断代码：是 */
    public static final String TRUE = "1";

    /** 判断代码：否 */
    public static final String FALSE = "0";

    /** 通用字符集编码 */
    public static final String CHARSET_UTF8 = "UTF-8";

    /** 中文字符集编码 */
    public static final String CHARSET_CHINESE = "GBK";

    /** 英文字符集编码 */
    public static final String CHARSET_LATIN = "ISO-8859-1";

    /** 根节点ID */
    public static final String ROOT_ID = "root";

    /** NULL字符串 */
    public static final String NULL = "null";

    /** 日期格式 */
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /** 日期时间格式 */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** 时间戳格式 */
    public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

    /** JSON成功标记 */
    public static final String JSON_SUCCESS = "success";

    /** JSON数据 */
    public static final String JSON_DATA = "data";

    /** JSON数据列表 */
    public static final String JSON_ROWS = "rows";

    /** JSON总数 */
    public static final String JSON_TOTAL = "total";

    /** JSON消息文本 */
    public static final String JSON_MESSAGE = "message";

    // 订单相关
    /** 货到付款 */
    public static final Integer CASH_ON_DELIVERY = 1;
    /** 在线支付 */
    public static final Integer ONLINE_PAYMENT = 2;
    /** 微信支付 */
    public static final Integer WEIXIN_PAYMENT = 3;
    /** 支付宝支付 */
    public static final Integer ALIPAY_PAYMENT = 4;

    /** 未付款 */
    public static final Integer NON_PAYMENT = 1;
    /** 已付款 */
    public static final Integer PAYMENT = 2;
    /** 未发货*/
    public static final Integer NON_SHIPMENTS = 3;
    /** 已发货 */
    public static final Integer SHIPMENTS = 4;
    /** 交易成功 */
    public static final Integer SUCCESSFUL = 5;
    /** 交易关闭 */
    public static final Integer CLOSE = 6;

    /**未评价*/
    public static final Integer EVALUATE_NO = 7;
    /**已评价*/
    public static final Integer EVALUATE_YES = 8;
    /**cookie 用户登录token*/
    public static final String TOKEN_LOGIN = "_xlg";
    /**cookie 购物车 key*/
    public static final String CART_KEY = "_xca";


    public static final String REDIS_VERSION = "1.0.0";
    public static final String SSO_VERSION = "1.0.0";
    public static final String NOTIFY_VERSION = "1.0.0";
    public static final String ADMIN_VERSION = "1.0.0";
    public static final String SEARCH_VERSION = "1.0.0";
    public static final String PORTAL_VERSION = "1.0.0";
    public static final String ORDER_VERSION = "1.0.0";
    public static final String CART_VERSION = "1.0.0";
    public static final String ITEM_VERSION = "1.0.0";

    /**
     * 属性文件加载对象
     */
    private static PropertyLoader loader = new PropertyLoader("base.properties");

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }
}
