/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-8-28
 */

package com.ylink.inetpay.cbs.bis.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.ylink.inetpay.cbs.bis.service.SchedulerManager;


/** 
 * @author LS
 * @date 2013-8-28
 * @description：启动初始化
 */
@Component
@Lazy(false)
public class SchedulerInit implements InitializingBean {

	@Autowired
	SchedulerManager schedulerManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		schedulerManager.initCronTrigger();
	}

}
