package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDto;

/**
 * 计划任务
 * 
 * @author LS
 * 
 */
public interface BisSchedPlanService {


	/**
	 * 所有明细执行完成后更新计划任务状态
	 * 
	 * @description
	 * @param jobPlanId
	 * @author LS
	 * @date 2013-8-29
	 */
	public void updateJobPlanStatusIfAllDetailFinished(String jobPlanId, Date finishTime);

	/**
	 * 该计划明细执行成功，则将依赖该计划明细的计划明细放入队列中
	 * 
	 * @description
	 * @param success
	 * @param detailId
	 * @author LS
	 * @date 2013-8-29
	 */
	public void executeRelyOnJobPlanDetail(String detailId,EAutoManual execType);

	/**
	 * 保存执行计划
	 * 
	 * @description
	 * @param plan
	 * @param details
	 * @author LS
	 * @date 2013-8-29
	 */
	public void save(BisSchedPlanDto plan, List<BisSchedPlanDetailDto> details);

	/**
	 * 明细入任务队列
	 * 
	 * @description
	 * @param pd
	 * @author LS
	 * @date 2013-8-29
	 */
	public void detailInQueue(BisSchedPlanDetailDto pd,EAutoManual execType);

	/**
	 * 计划详情加入队列
	 * 
	 * @description
	 * @param jobDetailId
	 * @author LS
	 * @date 2013-9-3
	 */
	public void detailInQueue(String jobDetailId,EAutoManual execType);

	/**
	 * 更新执行时间
	 * 
	 * @description
	 * @param id
	 * @param fireTime
	 * @author LS
	 * @date 2013-8-29
	 */
	public void updateFireTime(String id, Date fireTime);

	/**
	 * 更新完成时间
	 * 
	 * @description
	 * @param id
	 * @param finishTime
	 * @author LS
	 * @date 2013-8-29
	 */
	public void updateFinishTime(String id, Date finishTime);

	/**
	 * 获取计划列表
	 * 
	 * @description
	 * @param pageData
	 * @param jobPlanId
	 * @param startCreatedTime
	 * @param endCreatedTime
	 * @return
	 * @author LS
	 * @date 2013-9-4
	 */
	public PageData<BisSchedPlanDto> listJobPlanPage(PageData<BisSchedPlanDto> pageData,
			BisSchedPlanDto queryParam);
	
}
