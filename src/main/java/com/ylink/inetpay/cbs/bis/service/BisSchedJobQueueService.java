package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;

/**
 * 任务队列服务
 * 
 * @author LS
 * 
 */
public interface BisSchedJobQueueService {

	/**
	 * 删除队列
	 * 
	 * @description
	 * @param id
	 * @author LS
	 * @date 2013-9-4
	 */
	public void delete(String id);

	/**
	 * 删除队列
	 * 
	 * @description
	 * @param queueList
	 * @author LS
	 * @date 2013-9-4
	 */
	public void delete(List<BisSchedJobQueueDto> queueList);

	/**
	 * 更新队列
	 * 
	 * @description
	 * @param queue
	 * @author LS
	 * @date 2013-9-4
	 */
	public void update(BisSchedJobQueueDto queue);

	/**
	 * 更新队列
	 * 
	 * @description
	 * @param queueList
	 * @author LS
	 * @date 2013-9-4
	 */
	public void update(List<BisSchedJobQueueDto> queueList);

	/**
	 * 任务入队列
	 * 
	 * @description
	 * @param jobType
	 * @param refId
	 * @param invokeTime
	 * @author LS
	 * @date 2013-8-29
	 */
	public void jobInQueue(ESchedPlanType planType, String refId, Date invokeTime,EAutoManual execType);

	/**
	 * 任务入队列
	 * 
	 * @description
	 * @param jobType
	 * @param jobName
	 * @param maxSendCount
	 * @param refIds
	 * @param invokeTime
	 * @author LS
	 * @date 2013-8-29
	 */
	public void jobInQueue(ESchedPlanType planType,String[] refIds, Date invokeTime,EAutoManual execType);

	/**
	 * 工作任务队列大小
	 * 
	 * @description
	 * @return
	 * @author LS
	 * @date 2013-8-29
	 */
	public int getJobQueueSize();

	/**
	 * 工作任务队列大小
	 * 
	 * @description
	 * @param planType
	 *            工作任务类型
	 * @return
	 * @author LS
	 * @date 2013-8-29
	 */
	public int getJobQueueSize(ESchedPlanType planType);

	/**
	 * 根据工作任务类型查询工作队列
	 * 
	 * @description
	 * @param planType
	 *            工作任务类型
	 * @return
	 * @author LS
	 * @date 2013-8-29
	 */
	public List<BisSchedJobQueueDto> list(ESchedPlanType planType);

	/**
	 * 根据工作任务类型查询工作队列
	 * @param planType
	 * @return
	 */
	public List<BisSchedJobQueueDto> listWithLock(ESchedPlanType planType);
}
