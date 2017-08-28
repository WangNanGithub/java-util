package com.htouhui.pdl.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuchaoyang, chaoyang.liu@gmail.com
 * @version 1.0
 */
public class HttpRequestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtil.class);

    public static Map postRequest(String url, Map<String, Object> parameters) {
        try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(HttpSSLUtils.createSSLConnSocketFactory()).build()) {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            httpPost.setConfig(requestConfig);

            // 创建参数队列
            List<NameValuePair> params = new ArrayList<>();
            for (String key : parameters.keySet()) {
                params.add(new BasicNameValuePair(key, (String) parameters.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    LOGGER.error("HTTP_REQUEST FAILED, RESPONSE STATUS: " + statusCode);
                    return null;
                }
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity == null) {
                    LOGGER.error("HTTP INVOKE FAILED, RESPONSE OUTPUT IS NULL!");
                }
                try (InputStreamReader jsonReader = new InputStreamReader(responseEntity.getContent(), Charset.forName("UTF-8"))) {
                    Gson gson = new Gson();
                    Map jsonResult = gson.fromJson(jsonReader, Map.class);
                    return jsonResult;
                }
            } catch (IOException e) {
                LOGGER.error("HTTP_REQUEST_ERROR", e);
            }
        } catch (IOException e) {
            LOGGER.error("HTTPCLIENT_ERROR", e);
        }
        return null;
    }


    public static boolean testGetRequest(String url) {
        try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(HttpSSLUtils.createSSLConnSocketFactory()).build()) {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            httpGet.setConfig(requestConfig);

            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    return true;
                } else {
                    LOGGER.error("TEST|INVOKE|FAILED|RESPONSE_STATUS:{}|URL:{}", statusCode, url);
                    return false;
                }
            } catch (IOException e) {
                LOGGER.error("TEST|INVOKE|FAILED|HTTP_REQUEST_ERROR", e);
            }
        } catch (IOException e) {
            LOGGER.error("TEST|INVOKE|FAILED|HTTPCLIENT_ERROR", e);
        }
        return false;
    }

}
