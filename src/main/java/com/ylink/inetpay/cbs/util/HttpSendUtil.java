package com.ylink.inetpay.cbs.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylink.eu.util.tools.CharSetUtil;
import com.ylink.inetpay.common.core.constant.DataContentType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.GsonUtil;

public class HttpSendUtil {
	/**
	 * 日志记录器
	 */
	private static final Logger logger = LoggerFactory.getLogger(HttpSendUtil.class);

	/**
	 * 发送JSON数据
	 * 
	 * @param obj
	 * @param accessHttpUrl
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostJson(Object obj, String accessHttpUrl) {
		String retMsg = null;
		HttpResponse response = null;
		try {
			// 将obj转换为JSON
			String jsonStr = GsonUtil.noAnnotaGson.toJson(obj);
			
			logger.info("发往渠道的请求报文：{}", jsonStr);
			HttpClientServer hcu = new HttpClientServer();
			response = hcu.sendPostString(accessHttpUrl, jsonStr, DataContentType.JSON,CharSetUtil.UTF_8);
			if (response == null)
				return null;
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new HttpRuntimeException(EChannelProcState.SEND_FAILURE.getValue(),
						"statusCode=[" + response.getStatusLine().getStatusCode() + "],请检查连接");
			}
			// 获取服务器返回的字符串
			retMsg = EntityUtils.toString(response.getEntity(), CharSetUtil.UTF_8);
			logger.info("响应报文：{}", retMsg);
		} catch (Exception e) {
			logger.error("通过HttpClient服务发送JSON格式数据出现异常:{}", ExceptionProcUtil.getExceptionDesc(e));
			throw new HttpRuntimeException(EChannelProcState.SEND_FAILURE.getValue(), "通过HttpClient服务发送JSON格式数据出现异常.");
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					logger.error("EntityUtils.consume()出现异常:{}", ExceptionProcUtil.getExceptionDesc(e));
					throw new HttpRuntimeException(EChannelProcState.SYS_ERROR.getValue(),
							"EntityUtils.consume()出现异常.");
				}
			}
		}
		return retMsg;
	}
	
	public static String sendPostJsonStr(String jsonStr, String accessHttpUrl) {
		String retMsg = null;
		HttpResponse response = null;
		try {
			logger.info("发往渠道的请求报文：{}", jsonStr);
			HttpClientServer hcu = new HttpClientServer();
			response = hcu.sendPostString(accessHttpUrl, jsonStr, DataContentType.JSON,CharSetUtil.UTF_8);
			if (response == null)
				return null;
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new HttpRuntimeException(EChannelProcState.SEND_FAILURE.getValue(),
						"statusCode=[" + response.getStatusLine().getStatusCode() + "],请检查连接");
			}
			// 获取服务器返回的字符串
			retMsg = EntityUtils.toString(response.getEntity(), CharSetUtil.UTF_8);
			logger.info("响应报文：{}", retMsg);
		} catch (Exception e) {
			logger.error("通过HttpClient服务发送JSON格式数据出现异常:{}", ExceptionProcUtil.getExceptionDesc(e));
			throw new HttpRuntimeException(EChannelProcState.SEND_FAILURE.getValue(), "通过HttpClient服务发送JSON格式数据出现异常.");
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					logger.error("EntityUtils.consume()出现异常:{}", ExceptionProcUtil.getExceptionDesc(e));
					throw new HttpRuntimeException(EChannelProcState.SYS_ERROR.getValue(),
							"EntityUtils.consume()出现异常.");
				}
			}
		}
		return retMsg;
	}
	
	/**
	 * xml数据
	 * 
	 * @param obj
	 * @param accessHttpUrl
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostXml(String obj, String accessHttpUrl) {
		String retMsg = null;
		HttpResponse response = null;
		try {
			// 将obj转换为XML
//			String jsonStr = GsonUtil.noAnnotaGson.toJson(obj);
//			XStream xstream=new XStream();
//			String  xmlhead="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
//			String xml=xstream.toXML(obj);
//			String jsonStr=xmlhead+xml;
			
			logger.info("发往渠道的请求：{}", obj);
			HttpClientServer hcu = new HttpClientServer();
			response = hcu.sendPostString(accessHttpUrl, obj, DataContentType.XMLA,CharSetUtil.UTF_8);
			if (response == null)
				return null;
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new HttpRuntimeException(EChannelProcState.SEND_FAILURE.getValue(),
						"statusCode=[" + response.getStatusLine().getStatusCode() + "],请检查连接");
			}
			// 获取服务器返回的字符串
			retMsg = EntityUtils.toString(response.getEntity(), CharSetUtil.UTF_8);
			logger.info("响应报文：{}", retMsg);
		} catch (Exception e) {
			logger.error("通过HttpClient服务发送XML格式数据出现异常:{}", ExceptionProcUtil.getExceptionDesc(e));
			throw new HttpRuntimeException(EChannelProcState.SEND_FAILURE.getValue(), "通过HttpClient服务发送XML格式数据出现异常.");
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					logger.error("EntityUtils.consume()出现异常:{}", ExceptionProcUtil.getExceptionDesc(e));
					throw new HttpRuntimeException(EChannelProcState.SYS_ERROR.getValue(),
							"EntityUtils.consume()出现异常.");
				}
			}
		}
		return retMsg;
	}
	
	public static JSONArray parse(String protocolXML) {   
		JSONObject jsonObject = null;
		try{
			jsonObject = JSON.parseObject(protocolXML);
		}catch(Exception e){
		logger.error("json串转json对象时发生异常,json串="+protocolXML);
		throw new RuntimeException("json串转json对象时发生异常,json串="+protocolXML,e);
		}
		JSONObject itemsJsonObject = jsonObject.getJSONObject("List");
		JSONObject itemJsonObject = null;
		JSONArray itemJsonArray = null;
		Object itemObject = itemsJsonObject.get("item");
		if(itemObject instanceof JSONObject){
			itemJsonObject = (JSONObject)itemObject;
		}else if(itemObject instanceof JSONArray){
			itemJsonArray = (JSONArray)itemObject;
		}
		return itemJsonArray;
	}

}