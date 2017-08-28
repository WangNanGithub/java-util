package com.htouhui.pdl.util;

import com.google.gson.Gson;
import com.htouhui.pdl.dd.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由百度提供的工具类
 *
 * @author shenlinnan, linnan.shen@htouhui.com
 * @version 1.0
 */
public class BaiduUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaiduUtils.class);

    /**
     * 使用百度提供的功能查询号码相关信息
     *
     * @param mobile，要查询的电话号码
     * @return 百度返回的json字符串
     */
    public static String getMobileInfo(String mobile) {
        // 1.拼接参数字符串
        Map<String, Object> params = new HashMap<>();
        params.put(BaiduConstants.RESOURCE_NAME_KEY, BaiduConstants.RESOURCE_NAME_VALUE);
        params.put(BaiduConstants.QUERY_KEY, mobile);
        String baiduResult;
        try{
            // 2.从百度获取手机号的json字符串
            baiduResult = HttpClientUtil.httpGetRequest(BaiduConstants.BAIDU_USER_MOBILE_INFO_ADDRESS, params);
        } catch (URISyntaxException e) {
            // 如果失败了，可能百度这个链接地址变了或者是其他各种情况，还是走默认的逻辑，对整体的逻辑不会产生任何影响
            LOGGER.error("getMobileInfo|error", e);
            e.printStackTrace();
            baiduResult = "";
        }
        return baiduResult;
    }

    /**
     * 通过百度获取电话号码的省份，如河北，四川...
     *
     * @param mobile
     * @return
     */
    public static String getProv(String mobile) {
        Map properties = getProperties(mobile);
        if (properties == null || properties.size() <= 0) {
            LOGGER.warn("getProv|empty|{}|{}", mobile, properties);
            return "";
        }

        return (String) properties.get("prov");
    }

    /**
     * 获取运营商类型，如：中国移动、中国电信....
     *
     * @return
     */
    public static String getType(String mobile) {
        Map properties = getProperties(mobile);
        if (properties == null || properties.size() <= 0) {
            LOGGER.warn("getProv|empty|{}|{}", mobile, properties);
            return "";
        }

        return (String) properties.get("type");
    }

    /**
     * 去掉百度返回json里无用的信息包裹
     *
     * @param mobileInfo
     * @return
     */
    private static Map getDataDetail(String mobileInfo) {
        // 使用Gson解析json
        Map map = new Gson().fromJson(mobileInfo, Map.class);
        if (map == null || map.size() <= 0) {
            LOGGER.warn("isConfigTongDun|map|empty|baiduResult:{}|map:{}", mobileInfo, map);
            return null;
        }

        List data = (List) map.get("data");
        if (data == null || data.size() <= 0) {
            LOGGER.warn("isConfigTongDun|data|empty|baiduResult:{}|map:{}", mobileInfo, map);
            return null;
        }

        Map dataDetail = (Map) data.get(0);
        if (dataDetail == null || dataDetail.size() <= 0) {
            LOGGER.warn("dataDetail|empty|{}", data);
            return null;
        }
        return dataDetail;
    }

    /**
     * 获取电话号码的属性信息
     *
     * @param mobile
     * @return
     */
    private static Map getProperties(String mobile) {
        String mobileInfo = getMobileInfo(mobile);

        if (StringUtils.isBlank(mobileInfo)) {
            LOGGER.warn("getProv|mobileInfo|empty|{}|{}", mobileInfo, mobile);
            return null;
        }

        Map dataDetail = getDataDetail(mobileInfo);

        if (dataDetail == null || dataDetail.size() <= 0) {
            LOGGER.warn("getProv|dataDetail|empty|{}|{}", mobileInfo, mobile);
            return null;
        }

        return dataDetail;
    }

}
