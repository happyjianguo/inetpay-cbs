package com.ylink.inetpay.cbs.ucs.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserService;

/**
 * 初始化系统账户
 * 
 * @author LS
 * 
 */
@Component
@Lazy(false)
public class InitRootUser implements InitializingBean{

	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	
	@Autowired
	private UcsSecUserService loginAccountService;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			_log.info("初始化系统账户...");
			loginAccountService.initUcsSecUserDto();
		} catch (Exception e) {
			_log.error("初始化系统账户出错",e);
		}
	}
}
