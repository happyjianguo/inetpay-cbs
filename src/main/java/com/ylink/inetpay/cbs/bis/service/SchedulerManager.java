package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import com.ylink.inetpay.cbs.bis.pojo.TriggerWrap;

public interface SchedulerManager {

	/**
	 * 初始化CronTrigger
	 * 
	 * @description
	 * @author LS
	 * @date 2013-8-28
	 */
	public void initCronTrigger();

	/**
	 * 启动/停止Trigger
	 * 
	 * @description
	 * @param triggerName
	 * @author LS
	 * @date 2013-8-28
	 */
	public void toggleTrigger(String triggerName);

	/**
	 * 启动/停止Trigger
	 * 
	 * @description
	 * @param triggerName
	 * @param group
	 * @author LS
	 * @date 2013-8-28
	 */
	public void toggleTrigger(String triggerName, String group);

	/**
	 * 刷新定时任务
	 * 
	 * @description
	 * @param name
	 * @param group
	 * @author LS
	 * @date 2013-8-28
	 */
	public void refresh(String name, String group);

	/**
	 * 列出所有Trigger
	 * 
	 * @description
	 * @return
	 * @author LS
	 * @date 2013-8-28
	 */
	public List<TriggerWrap> listAllTrigger();

	/**
	 * 获取触发器
	 * 
	 * @description
	 * @param group
	 * @param name
	 * @return
	 * @author LS
	 * @date 2013-8-28
	 */
	public TriggerWrap get(String group, String name);

	/**
	 * 更新时钟
	 * 
	 * @description
	 * @param name
	 * @param group
	 * @param cronExp
	 * @author LS
	 * @date 2013-8-28
	 */
	public void updateCronExp(String name, String group, String cronExp);
	/**
	 * 启用户停用定时任务
	 * @param triggerName
	 * @param group
	 * @param isEnAble 是否启用
	 */
	public void toggleTriggerEnAble(String triggerName, String group,boolean isEnAble);

}
