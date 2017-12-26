package com.ylink.inetpay.cbs.util;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

public class MyMapperScannerConfigurer extends MapperScannerConfigurer {
	
	public void setApplicationContext(ApplicationContext applicationContext){
		super.setApplicationContext(applicationContext);
	    setAnnotationClass(MybatisMapper.class);
	}
}