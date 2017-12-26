package com.ylink.inetpay.cbs.common;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylink.inetpay.common.core.util.ExceptionProcUtil;

public class CbsConfig {
	
	private static Logger _log = LoggerFactory.getLogger(CbsConfig.class);
	
	private static String LOG_FULL_PATH;
	
	static {
		Properties props = new Properties();
		try {
			props.load(CbsConfig.class.getResourceAsStream("/properties/config.properties"));
			LOG_FULL_PATH = props.getProperty("log_full_path", "");
		} catch (IOException e) {
			_log.error("加载config.properties配置文件失败" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}
	
	public static String getLogFullPath(){
		return LOG_FULL_PATH;
	}
	
	public static void main(String[] args) {
		System.out.println(getLogFullPath());
	}
}
