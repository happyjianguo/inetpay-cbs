package com.ylink.inetpay.cbs.util;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylink.inetpay.common.core.constant.DataContentType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;

/**
 * HttpClient服务
 * 
 * @author lhui
 * @date 2016-10-22
 */
public class HttpClientServer {
	protected static final Logger logger = LoggerFactory.getLogger(HttpClientServer.class);
	// 请求超时时间
	private int connectionRequestTimeout = 1000;

	// 连接超时时间，默认10秒
	private int socketTimeout = 10000;

	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;

	private HttpClient httpClient;

	@SuppressWarnings("deprecation")
	private HttpClient getHttpClient() {
		if (null != httpClient) {
			return httpClient;
		}
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
		return httpClient;
	}

	/**
	 * 通过POST方式提交一个字符串
	 * 
	 * @param targetUrl 目标访问URL
	 * @param data 发送的字符串数据，可以是xml,json等数据
	 * @param enCoding 编码
	 * @return 接收方返回的消息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse sendPostString(String targetUrl, String data, DataContentType dataType, String enCoding)
			throws ClientProtocolException, IOException {
		HttpResponse response = null;
		HttpPost httpPost = null;
		if (null == httpClient) {
			httpClient = getHttpClient();
		}
		if (targetUrl.startsWith("https")) {
			enableSSL(httpClient);
		}

		httpPost = new HttpPost(targetUrl);
//		httpPost.setConfig(RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
//				.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build());
		StringEntity postEntity = new StringEntity(data, enCoding);
		httpPost.addHeader("Content-Type", dataType.getValue());
		httpPost.setEntity(postEntity);
		response = httpClient.execute(httpPost);
		return response;
	}

	private static void enableSSL(HttpClient httpclient) {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry().register(https);
		} catch (Exception e) {
			logger.error("系统异常，异常原因：{}",ExceptionProcUtil.getExceptionDesc(e));
		}
	}

	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};
}
