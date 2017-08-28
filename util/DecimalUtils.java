package com.htouhui.pdl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 针对BigDecimal类型的工具类
 *
 * @author shenlinnan, linnan.shen@htouhui.com
 * @version 1.0
 */
public class DecimalUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecimalUtils.class);

    /**
     * 将BigDecimal数据类型转成字符串，同时设定格式为小数点后两位
     *
     * @param resource
     * @return
     */
    public static String convertBigDecimalToString(BigDecimal resource) {
        if (resource == null) {
            LOGGER.warn("convertBigDecimalToString|parameter|empty|{}", resource);
            return "0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(resource);
    }

    /**
     * 如果出现类型转换的异常，则继续发送短信，记录错误日志
     *
     * @param resource
     * @return
     */
    public static String convertBigDecimalToString(Object resource) {
        BigDecimal bigDecimal;
        try{
            bigDecimal = (BigDecimal) resource;
        } catch (ClassCastException e) {
            LOGGER.error("转换费用时失败：", e);
            return "0.00";
        }
        return convertBigDecimalToString(bigDecimal);
    }

}
