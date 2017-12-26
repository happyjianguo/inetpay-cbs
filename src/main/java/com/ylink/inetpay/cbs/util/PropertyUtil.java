package com.ylink.inetpay.cbs.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylink.inetpay.common.core.util.ExceptionProcUtil;

public class PropertyUtil {
	protected static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static Properties props;
	static { 
		// 加载属性文件   "classpath:propertiesconfig.properties"
		props = new Properties();
		try {

			InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("properties/config.properties");
			props.load(inputStream);
		} catch (Exception e) {
			logger.error("系统异常，异常原因：{}",ExceptionProcUtil.getExceptionDesc(e));
		}
	}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
}
