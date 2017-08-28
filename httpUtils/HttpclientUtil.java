package com.htouhui.pdl.thirdparty.baidu.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient 工具类
 *
 * @author WangNan, nan.wang@htouhui.com
 * @version 1.0
 */
public class HttpclientUtil {
	
	private static final Log logger = LogFactory.getLog(HttpclientUtil.class);
	
	private static HttpClient client = HttpClients.createDefault();
	
	/**
	 * 配置初始化
	 */
	private static void init() {}
	
	/**
	 * 把响应实转为json数组
	 * 
	 * @param baseUrl
	 * @param jsonParams
	 * @return
	 */
	public static String post(String baseUrl, String jsonParams) {
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(baseUrl);
			httpPost.setHeader("charset", "UTF-8");
			StringEntity entity = new StringEntity(jsonParams, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse res = client.execute(httpPost);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.POST异常:", e);
			return "{success:'0',msg:'POST请求抛锚UnsupportedEncodingException'}";
		}
		catch (ClientProtocolException e) {
			logger.error("HttpUtil.POST异常:", e);
			return "{success:'0',msg:'POST请求抛锚ClientProtocolException'}";
		}
		catch (IOException e) {
			logger.error("HttpUtil.POST异常:", e);
			return "{success:'0',msg:'POST请求抛锚IOException'}";
		}
		catch (Exception e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚Exception'}";
		}
		finally {
			httpPost.releaseConnection();
		}
	}
	
	/**
	 * @功能描述: 以POST方式请求外部URL
	 * @param baseUrl url 请求地址
	 * @param params 请求参数
	 * @return 以JSON字符串的方式返回请结果
	 */
	public static String post(String baseUrl, Map<String, String> params) {
		
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(baseUrl);
			logger.info("请求地址为:" + baseUrl);
			httpPost.setHeader("charset", "UTF-8");
			
			List<NameValuePair> paires = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paires.add(new BasicNameValuePair(entry.getKey(), entry.getValue() != null ? entry.getValue() : ""));
			}
			
			HttpEntity entity = new UrlEncodedFormEntity(paires, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse res = client.execute(httpPost);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.POST异常:", e);
			return "{success:'0',msg:'POST请求抛锚UnsupportedEncodingException'}";
		}
		catch (ClientProtocolException e) {
			logger.error("HttpUtil.POST异常:", e);
			return "{success:'0',msg:'POST请求抛锚ClientProtocolException'}";
		}
		catch (IOException e) {
			logger.error("HttpUtil.POST异常:", e);
			return "{success:'0',msg:'POST请求抛锚IOException'}";
		}
		catch (Exception e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚Exception'}";
		}
		finally {
			httpPost.releaseConnection();
		}
	}
	
	/**
	 * function : 以GET方式请求外部URL
	 *
	 * @param url url请求地址
	 * @return 以JSON字符串的方式返回请结果
	 */
	public static String get(String url) {
		init();
		String result = null;
		if (url != null && !"".equals(url)) {
			return result;
		}
		HttpGet httpGet = null;
		int i = 0;
		while (true) {
			try {
				httpGet = new HttpGet(url);
				httpGet.setHeader("charset", "UTF-8");
				HttpResponse res = client.execute(httpGet);
				int status = res.getStatusLine().getStatusCode();
				if (status < 200 || status >= 300) {
					throw new ClientProtocolException("请求出现异常，状态码为：" + status);
				}
				HttpEntity entityRes = res.getEntity();
				result = EntityUtils.toString(entityRes, "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				logger.error("HttpUtil.GET异常:", e);
				return "{success:'0',msg:'GET请求抛锚UnsupportedEncodingException'}";
			}
			catch (ClientProtocolException e) {
				logger.error("HttpUtil.GET异常:", e);
				return "{success:'0',msg:'GET请求抛锚ClientProtocolException'}";
			}
			catch (IOException e) {
				logger.error("HttpUtil.GET异常:", e);
				return "{success:'0',msg:'GET请求抛锚IOException'}";
			}
			catch (Exception e) {
				logger.error("HttpUtil.GET异常:", e);
				return "{success:'0',msg:'GET请求抛锚Exception'}";
			}
			if (result != null && !"".equals(result)) {
				break;
			} else {
				if (i < 5) {
					try {
						i++;
						Thread.sleep(100);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
			}
		}
		return result;
	}
}
