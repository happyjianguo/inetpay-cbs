/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-8-27
 */
package com.ylink.inetpay.cbs.bis.factory;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.MethodInvoker;

/** 
 * @author LS
 * @date 2013-8-27
 * @description：
 */
public class BeanInvokingJobDetailFactoryBean implements
		FactoryBean<JobDetail>, BeanNameAware, InitializingBean,
		ApplicationContextAware {
	protected static ApplicationContext applicationContext;
	private static Logger logger = LoggerFactory
			.getLogger(BeanInvokingJobDetailFactoryBean.class);

	private String group = Scheduler.DEFAULT_GROUP;

	private boolean concurrent = true;

	private boolean durable = false;

	private boolean shouldRecover = false;
	private String[] jobListenerNames;
	private String beanName;
	private JobDetail jobDetail;
	private String targetBean;
	private String targetMethod;
	private Object[] arguments;
	private String description;

	public String getTargetBean() {
		return this.targetBean;
	}

	public void setTargetBean(String targetBean) {
		this.targetBean = targetBean;
	}

	public String getTargetMethod() {
		return this.targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public JobDetail getObject() throws Exception {
		return this.jobDetail;
	}

	public Class<JobDetail> getObjectType() {
		return JobDetail.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			logger.debug("start");

			logger.debug("Creating JobDetail " + this.beanName);

			JobBuilder jobBuilder = JobBuilder
					.newJob(this.concurrent ? BeanInvokingJob.class
							: StatefulBeanInvokingJob.class)
					.withIdentity(this.beanName, this.group)
					.requestRecovery(this.shouldRecover)
					.storeDurably(this.durable)
					.withDescription(this.description);

			this.jobDetail = jobBuilder.build();

			this.jobDetail.getJobDataMap().put("targetBean", this.targetBean);
			this.jobDetail.getJobDataMap().put("targetMethod",
					this.targetMethod);
			this.jobDetail.getJobDataMap().put("arguments", this.arguments);

			logger.debug("Registering JobListener names with JobDetail object "
					+ this.beanName);
			if (this.jobListenerNames != null) {
				for (int i = 0; i < this.jobListenerNames.length; i++)
					;
			}

			logger.info("创建任务[JobDetail]: " + this.jobDetail + "; targetBean: "
					+ this.targetBean + "; targetMethod: " + this.targetMethod
					+ "; arguments: " + this.arguments + ";");
		} finally {
			logger.debug("end");
		}
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setJobListenerNames(String[] jobListenerNames) {
		this.jobListenerNames = jobListenerNames;
	}

	public void setShouldRecover(boolean shouldRecover) {
		this.shouldRecover = shouldRecover;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public static class BeanInvokingJob implements Job {
		private static Logger logger = LoggerFactory
				.getLogger(BeanInvokingJob.class);

		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			String targetBean = null;
			String targetMethod = null;
			try {
				logger.debug("start");

				targetBean = context.getMergedJobDataMap().getString(
						"targetBean");
				logger.debug("targetBean is " + targetBean);
				if (targetBean == null) {
					throw new JobExecutionException(
							"targetBean cannot be null.", false);
				}
				targetMethod = context.getMergedJobDataMap().getString(
						"targetMethod");
				logger.debug("targetMethod is " + targetMethod);
				if (targetMethod == null) {
					throw new JobExecutionException(
							"targetMethod cannot be null.", false);
				}

				Object argumentsObject = context.getMergedJobDataMap().get(
						"arguments");
				Object[] arguments = (argumentsObject instanceof String) ? null
						: (Object[]) argumentsObject;
				logger.debug("arguments array is " + arguments);

				Object bean = BeanInvokingJobDetailFactoryBean.applicationContext
						.getBean(targetBean);
				logger.debug("applicationContext resolved bean name/id '"
						+ targetBean + "' to " + bean);

				MethodInvoker beanMethod = new MethodInvoker();
				beanMethod.setTargetObject(bean);
				beanMethod.setTargetMethod(targetMethod);
				beanMethod.setArguments(arguments);
				beanMethod.prepare();

				logger.debug("Invoking Bean: " + targetBean + "; Method: "
						+ targetMethod + "; arguments: " + arguments + ";");

				beanMethod.invoke();
			} catch (JobExecutionException e) {
				throw e;
			} catch (Exception e) {
				logger.error("定时任务[bean=" + targetBean + ",method="
						+ targetMethod + "]执行异常：", e);
				throw new JobExecutionException(e);
			} finally {
				logger.debug("end");
			}
		}
	}

	public static class StatefulBeanInvokingJob extends
			BeanInvokingJobDetailFactoryBean.BeanInvokingJob implements
			StatefulJob {
	}
}