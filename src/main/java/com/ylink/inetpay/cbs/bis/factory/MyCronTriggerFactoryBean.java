/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-8-27
 */

package com.ylink.inetpay.cbs.bis.factory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Constants;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * @author LS
 * @date 2013-8-27
 * @description：TODO
 */
public class MyCronTriggerFactoryBean implements FactoryBean<CronTrigger>,
		BeanNameAware, InitializingBean {
	private static final Constants constants = new Constants(CronTrigger.class);
	private String name;
	private String group;
	private JobDetail jobDetail;
	private JobDataMap jobDataMap = new JobDataMap();
	private Date startTime;
	private long startDelay;
	private String cronExpression;
	private TimeZone timeZone;
	private int priority;
	private int misfireInstruction;
	private String beanName;
	private CronTrigger cronTrigger;
	protected String description;

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public void setJobDataMap(JobDataMap jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

	public JobDataMap getJobDataMap() {
		return this.jobDataMap;
	}

	public void setJobDataAsMap(Map<String, ?> jobDataAsMap) {
		this.jobDataMap.putAll(jobDataAsMap);
	}

	public void setStartDelay(long startDelay) {
		Assert.isTrue(startDelay >= 0L, "Start delay cannot be negative");
		this.startDelay = startDelay;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setMisfireInstruction(int misfireInstruction) {
		this.misfireInstruction = misfireInstruction;
	}

	public void setMisfireInstructionName(String constantName) {
		this.misfireInstruction = constants.asNumber(constantName).intValue();
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void afterPropertiesSet() {
		if (this.name == null) {
			this.name = this.beanName;
		}
		if (this.group == null) {
			this.group = "DEFAULT";
		}
		if (this.jobDetail != null) {
			this.jobDataMap.put("jobDetail", this.jobDetail);
		}
		if (this.startDelay > 0L) {
			this.startTime = new Date(System.currentTimeMillis()
					+ this.startDelay);
		} else if (this.startTime == null) {
			this.startTime = new Date();
		}
		if (this.timeZone == null) {
			this.timeZone = TimeZone.getDefault();
		}
		Method jobKeyMethod = null;
		Class<?> cronTriggerClass = null;
		try {
			cronTriggerClass = getClass().getClassLoader().loadClass(
					"org.quartz.impl.triggers.CronTriggerImpl");
			jobKeyMethod = JobDetail.class.getMethod("getKey", new Class[0]);
		} catch (ClassNotFoundException ex) {
			cronTriggerClass = CronTrigger.class;
			jobKeyMethod = null;
		} catch (NoSuchMethodException ex) {
			throw new IllegalStateException("Incompatible Quartz version");
		}
		
		BeanWrapper bw = new BeanWrapperImpl(cronTriggerClass);
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.add("name", this.name);
		pvs.add("group", this.group);
		if (jobKeyMethod != null) {
			pvs.add("jobKey",
					ReflectionUtils.invokeMethod(jobKeyMethod, this.jobDetail));
		} else {
			pvs.add("jobName", this.jobDetail.getKey().getName());
			pvs.add("jobGroup", this.jobDetail.getKey().getGroup());
		}
		pvs.add("jobDataMap", this.jobDataMap);
		pvs.add("startTime", this.startTime);
		pvs.add("cronExpression", this.cronExpression);
		pvs.add("timeZone", this.timeZone);
		pvs.add("priority", Integer.valueOf(this.priority));
		pvs.add("misfireInstruction", Integer.valueOf(this.misfireInstruction));
		pvs.add("description", this.description);
		bw.setPropertyValues(pvs);
		this.cronTrigger = ((CronTrigger) bw.getWrappedInstance());
	}

	public CronTrigger getObject() {
		return this.cronTrigger;
	}

	public Class<?> getObjectType() {
		return CronTrigger.class;
	}

	public boolean isSingleton() {
		return true;
	}
}