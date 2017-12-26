package com.ylink.inetpay.cbs.bis.service;

import java.io.Serializable;

/**
 * 定时任务接口
 * 
 * @author LS
 * 
 */

public interface SchedulerService extends Serializable {
	/**
	 * 定时任务执行入口
	 */
	public void execute();

}
