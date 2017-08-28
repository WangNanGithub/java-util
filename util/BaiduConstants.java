package com.htouhui.pdl.util;

/**
 * 百度工具类使用的常量类
 *
 * @author shenlinnan, linnan.shen@htouhui.com
 * @version 1.0
 */
public class BaiduConstants {

    /**
     * 百度查询电话号码归属地的http地址
     * 参数：resource_name=guishudi&query=13358703419
     */
    public static final String BAIDU_USER_MOBILE_INFO_ADDRESS = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";

    /**
     * 第一个参数的key
     */
    public static final String RESOURCE_NAME_KEY = "resource_name";

    /**
     * 第一个参数的value
     */
    public static final String RESOURCE_NAME_VALUE = "guishudi";

    /**
     * 第二个参数的key，后面跟的是要查询的电话号码
     */
    public static final String QUERY_KEY = "query";

}
