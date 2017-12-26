/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 */

package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.pojo.Result;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;

/**
 * 批次执行
 * 
 * @author LS
 * 
 */
public abstract class BatchSchedulerService extends AbstractSchedulerService {

	private static final long serialVersionUID = -398051282193654224L;

	@Autowired
	protected BisSchedPlanService jobPlanService;

	@Autowired
	protected BisSchedPlanDetailService jobPlanDetailService;

	/**
	 * 批次明细执行
	 * 
	 * @description
	 * @param jq
	 * @param detail
	 * @param mapResults
	 * @return
	 * @author LS
	 * @date 2013-9-4
	 */
	public abstract Result doBatchBusiness(BisSchedJobQueueDto jq, BisSchedPlanDetailDto detail);

	/**
	 * 依赖检查
	 * 
	 * @description
	 * @param jq
	 * @param jobPlanDetail
	 * @param mapResults
	 * @return true：则表示有依赖的任务没有完成,false：则表示无依赖的任务或依赖的任务都已经完成
	 * @author LS
	 * @date 2013-9-4
	 */
	protected boolean checkRelyOn(BisSchedJobQueueDto jq, BisSchedPlanDetailDto jobPlanDetail) {
		if (StringUtils.isNotBlank(jobPlanDetail.getPreDetailId())) {
			long count = jobPlanDetailService.countUnSuccessPlanDetailsByDetailIds(jobPlanDetail.getPreDetailId());
			return !(count == 0);
		}
		return false;
	}

	@Override
	public List<Result> doBusiness(List<BisSchedJobQueueDto> items) {

		List<Result> results = new ArrayList<>();
		Result result = null;

		for (BisSchedJobQueueDto item : items) {
			BisSchedPlanDetailDto detail = jobPlanDetailService.getJobDetail(item.getRefId());
			item.setInvokeTime(new Date());
			result = doBatchBusiness(item, detail);
			results.add(result);
			
			/*if(results.size() == 1) {
				// 任务执行时间
				jobPlanService.updateFireTime(detail.getPlanId(), new Date());
			}*/
		}
		return results;
	}

	@Override
	public void postJobExecute(List<Result> results) {
		jobPlanDetailService.updateDetailStatus(results);
		for (Result result : results) {
			if (result.getStatus() == ESchedJobQueueStatus.SUCCESS) {
				jobPlanService.executeRelyOnJobPlanDetail(result.getPlanDetaiId(),EAutoManual.AUTO);
			}
		}
	}

}
