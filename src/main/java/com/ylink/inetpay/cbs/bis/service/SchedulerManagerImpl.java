package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.util.StringUtil;

import com.ylink.inetpay.cbs.bis.pojo.TriggerWrap;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EIsEnabled;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobIntervalDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class SchedulerManagerImpl implements SchedulerManager {

	private static Logger _log = LoggerFactory
			.getLogger(SchedulerManagerImpl.class);

	@Autowired
	private BisSchedJobIntervalService bisSchedJobIntervalService;

	private StdScheduler scheduler;

	public void setQuartzScheduler(StdScheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 获取触发器
	 */
	public TriggerWrap get(String group, String name) {
		TriggerWrap wrap = null;
		try {
			Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(name,
					group));
			wrap = new TriggerWrap(trigger);
			wrap.setState(getTriggerState(trigger.getKey()));
		} catch (SchedulerException e) {
			_log.error("获取触发器失败", e);
		}
		return wrap;
	}

	/**
	 * 更新时钟
	 */
	public void updateCronExp(String name, String group, String cronExp) {
		
		Trigger trigger;
		try {
			trigger = scheduler.getTrigger(TriggerKey.triggerKey(name, group));
		} catch (SchedulerException e) {
			_log.error("获取触发器失败，失败原因：{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"获取触发器失败：name=" + name);
		}

		boolean enabled = !TriggerState.PAUSED.equals(getTriggerState(trigger.getKey()));
		
		BisSchedJobIntervalDto jobTrigger = bisSchedJobIntervalService.get(name);
		
		if (jobTrigger == null) {
			jobTrigger = new BisSchedJobIntervalDto();
			jobTrigger.setTriggerName(name);
			jobTrigger.setCronExpression(cronExp);
			jobTrigger.setEnabled(EIsEnabled.ENABLED);
			bisSchedJobIntervalService.save(jobTrigger);
		} else {
			jobTrigger.setCronExpression(cronExp);
			bisSchedJobIntervalService.update(jobTrigger);
		}
		freshTrigger(trigger, enabled, cronExp);
	}

	/**
	 * 获得系统Trigger
	 */
	private List<Trigger> listTriggers() {
		List<Trigger> triggers = new ArrayList<Trigger>();
		List<String> groups = null;
		try {
			groups = scheduler.getTriggerGroupNames();
			if(groups == null){
				groups = new ArrayList<String>();
			}
			groups.add(Scheduler.DEFAULT_GROUP);
		} catch (Exception e) {
		}
		try {
			for (String group : groups) {
				Set<TriggerKey> keys = scheduler.getTriggerKeys(GroupMatcher
						.triggerGroupContains(group));
				for (TriggerKey key : keys)
					triggers.add(scheduler.getTrigger(key));
			}
		} catch (Exception e) {
			_log.error("获取系统定时任务异常：" + ExceptionProcUtil.getExceptionDesc(e));
		}
		return triggers;
	}

	/**
	 * 定时器设置
	 */
	private Map<String, BisSchedJobIntervalDto> getJobIntervalMap() {
		List<BisSchedJobIntervalDto> list = bisSchedJobIntervalService.listAll();
		if (list == null || list.isEmpty())
			return null;
		Map<String, BisSchedJobIntervalDto> jiMap = new HashMap<String, BisSchedJobIntervalDto>();
		for (BisSchedJobIntervalDto ji : list)
			jiMap.put(ji.getTriggerName(), ji);
		return jiMap;
	}

	@Override
	public void initCronTrigger() {
		
		Map<String, BisSchedJobIntervalDto> jiMap = getJobIntervalMap();
		if (jiMap == null || jiMap.isEmpty())
			return;

		List<Trigger> triggers = listTriggers();
		if (triggers == null || triggers.isEmpty())
			return;

		// 针对每个Trigger 设置Cron表示达
		for (Trigger trigger : triggers) {
			BisSchedJobIntervalDto jobTrigger = jiMap.get(trigger.getKey().getName());
			if (jobTrigger != null && trigger instanceof CronTrigger) {
				freshTrigger(trigger, jobTrigger.isRuning(),jobTrigger.getCronExpression());
			}
		}
	}

	/**
	 * 刷新定时任务
	 */
	public void refresh(String name, String group) {
		if (scheduler == null)
			return;

		try {
			Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(name,group));
			
			if (trigger != null && trigger instanceof CronTrigger) {
				BisSchedJobIntervalDto jobTrigger = bisSchedJobIntervalService.get(name);
				if (jobTrigger != null) {
					CronScheduleBuilder csb = CronScheduleBuilder
							.cronSchedule(jobTrigger.getCronExpression());

					trigger = TriggerBuilder.newTrigger()
							.withIdentity(trigger.getKey())
							.withDescription(trigger.getDescription())
							.withSchedule(csb).build();
					scheduler.rescheduleJob(trigger.getKey(), trigger);
					if (!jobTrigger.isRuning())
						scheduler.pauseTrigger(trigger.getKey());
				}
			}
		} catch (SchedulerException e) {
			_log.error("获取触发器失败，失败原因：{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"获取触发器失败：name=" + name);
		}

	}

	/**
	 * 刷新触发器
	 */
	private void freshTrigger(Trigger trigger, boolean run, String cronExp) {
		if (scheduler == null)
			return;
		try {
			// 重设置trigger
			CronScheduleBuilder csb = CronScheduleBuilder
					.cronSchedule(cronExp);
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(trigger.getKey())
					.withDescription(trigger.getDescription())
					.withSchedule(csb).build();
			scheduler.rescheduleJob(trigger.getKey(), trigger);
			if (!run) { 
				// 停用
				scheduler.pauseTrigger(trigger.getKey());
			} 
		} catch (Exception e) {
			_log.error("刷新触发器失败",e);
		}
	}

	/**
	 * 定时器状态
	 */
	protected TriggerState getTriggerState(TriggerKey key) {
		try {
			return scheduler.getTriggerState(key);
		} catch (SchedulerException e) {
			_log.error("", e);
		}
		return TriggerState.NONE;
	}

	@Override
	public List<TriggerWrap> listAllTrigger() {
		List<TriggerWrap> result = null;
		List<Trigger> list = listTriggers();

		if (list != null && !list.isEmpty()) {
			result = new ArrayList<TriggerWrap>();
			for (Trigger trigger : list) {
				TriggerWrap wrap = new TriggerWrap(trigger);
				wrap.setState(getTriggerState(trigger.getKey()));
				result.add(wrap);
			}
		}
		return result;
	}

	/**
	 * 启动或暂停定时器
	 */
	public void toggleTrigger(String triggerName) {
		toggleTrigger(triggerName, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * 启动或暂停定时器
	 */
	public void toggleTrigger(String triggerName, String group) {
		if (StringUtil.clean(triggerName) == null)
			return;

		String cronExpression = null;
		BisSchedJobIntervalDto jobTrigger = bisSchedJobIntervalService.get(triggerName);
		if (jobTrigger != null) {
			jobTrigger.setEnabled(jobTrigger.isRuning()?EIsEnabled.UNENABLED:EIsEnabled.ENABLED);
			cronExpression = StringUtil.clean(jobTrigger.getCronExpression());
		}
		Trigger target = null;
		try {
			target = scheduler.getTrigger(TriggerKey.triggerKey(triggerName,
					group));
		} catch (SchedulerException e) {
			_log.error("获取触发器失败，失败原因：{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"获取触发器失败：triggerName=" + triggerName);
		}

		boolean enabled = false;
		try {
			TriggerState state = scheduler.getTriggerState(target.getKey());

			if (target instanceof CronTrigger) {
				cronExpression = ((CronTrigger) target).getCronExpression();
			}

			if (TriggerState.PAUSED.equals(state)) {
				scheduler.resumeTrigger(target.getKey());// 启动定时器
				enabled = true;
			} else if (TriggerState.NORMAL.equals(state)) {
				scheduler.pauseTrigger(target.getKey());// 暂停定时器
			}
		} catch (SchedulerException e) {
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"更该触发器状态失败["
					+ triggerName + "]");
		}
		if (jobTrigger == null) {
			jobTrigger = new BisSchedJobIntervalDto();
			jobTrigger.setEnabled(enabled?EIsEnabled.ENABLED:EIsEnabled.UNENABLED);
			jobTrigger.setCronExpression(cronExpression);
			jobTrigger.setTriggerName(triggerName);
			bisSchedJobIntervalService.save(jobTrigger);
		}else{
			jobTrigger.setEnabled(enabled?EIsEnabled.ENABLED:EIsEnabled.UNENABLED);
			bisSchedJobIntervalService.update(jobTrigger);
		}
	}
	
	/**
	 * 启用或者停用
	 * @param triggerName
	 * @param group
	 * @param isEnAble
	 */
	public void toggleTriggerEnAble(String triggerName, String group,boolean isEnAble) {
		if (StringUtil.clean(triggerName) == null)
			return;

		String cronExpression = null;
		BisSchedJobIntervalDto jobTrigger = bisSchedJobIntervalService.get(triggerName);
		if (jobTrigger != null) {
			jobTrigger.setEnabled(jobTrigger.isRuning()?EIsEnabled.UNENABLED:EIsEnabled.ENABLED);
			cronExpression = StringUtil.clean(jobTrigger.getCronExpression());
		}
		Trigger target = null;
		try {
			target = scheduler.getTrigger(TriggerKey.triggerKey(triggerName,
					group));
		} catch (SchedulerException e) {
			_log.error("获取触发器失败，失败原因：{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"获取触发器失败：triggerName=" + triggerName);
		}

		boolean enabled = false;
		try {
			TriggerState state = scheduler.getTriggerState(target.getKey());

			if (target instanceof CronTrigger) {
				cronExpression = ((CronTrigger) target).getCronExpression();
			}
			if(isEnAble){
				if (TriggerState.PAUSED.equals(state)) {
					scheduler.resumeTrigger(target.getKey());// 启动定时器
					enabled = true;
				} 
			}else{
				if (TriggerState.NORMAL.equals(state)) {
					scheduler.pauseTrigger(target.getKey());// 暂停定时器
				}
			}
		} catch (SchedulerException e) {
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"更该触发器状态失败["
					+ triggerName + "]");
		}
		if (jobTrigger == null) {
			jobTrigger = new BisSchedJobIntervalDto();
			jobTrigger.setEnabled(enabled?EIsEnabled.ENABLED:EIsEnabled.UNENABLED);
			jobTrigger.setCronExpression(cronExpression);
			jobTrigger.setTriggerName(triggerName);
			bisSchedJobIntervalService.save(jobTrigger);
		}else{
			jobTrigger.setEnabled(enabled?EIsEnabled.ENABLED:EIsEnabled.UNENABLED);
			bisSchedJobIntervalService.update(jobTrigger);
		}
	}
	
}
