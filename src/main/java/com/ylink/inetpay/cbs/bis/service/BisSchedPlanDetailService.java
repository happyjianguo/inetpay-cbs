/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-9-4
 */

package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ylink.inetpay.cbs.bis.pojo.Result;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;

/**
 * @author LS
 * @date 2013-9-4
 * @description：
 */
public interface BisSchedPlanDetailService {

	/**
	 * 更新任务计划详情
	 * 
	 * @description
	 * @param jobPlanDetail
	 * @author LS
	 * @date 2013-9-4
	 */
	public void updateJobDetail(BisSchedPlanDetailDto jobPlanDetail);

	/**
	 * 计划明细
	 * 
	 * @description
	 * @param id
	 * @return
	 * @author LS
	 * @date 2013-8-29
	 */
	public BisSchedPlanDetailDto getJobDetail(String id);

	/**
	 * 更新明细状态
	 * 
	 * @description
	 * @param detailId
	 * @param status
	 * @author LS
	 * @date 2013-8-29
	 */
	public void updateJobPlanDetailStatus(String detailId,ESchedJobQueueStatus status);

	/**
	 * 更新明细状态
	 * 
	 * @description
	 * @param detailId
	 * @param success
	 * @param errMsg
	 * @author LS
	 * @date 2013-8-29
	 */
	public void updateJobPlanDetailStatus(String detailId, boolean success,String errMsg);

	/**
	 * 更新任务计划明细数据状态
	 * 
	 * @description
	 * @param results
	 * @author LS
	 * @date 2013-8-29
	 */
	public void updateDetailStatus(List<Result> results);

	/**
	 * 查询依赖未完成任务
	 * 
	 * @description
	 * @param jobDetailId
	 * @return
	 * @author LS
	 * @date 2013-8-29
	 */
	public List<BisSchedPlanDetailDto> listRelyOnUnsuccessful(String jobDetailId);
	
	/**
	 * 执行计划明细
	 * 
	 * @description
	 * @param planId
	 *            计划任务ID
	 * @return
	 * @author LS
	 * @date 2013-8-29
	 */
	public List<BisSchedPlanDetailDto> listJobPlanDetail(String planId);
	
	/**
	 * 保存任务计划详情
	 * 
	 * @description
	 * @param record
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	int save(BisSchedPlanDetailDto record);

	/**
	 * 批量保存任务计划详情
	 * 
	 * @description
	 * @param jpdList
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	int saveList(@Param("jpdList") List<BisSchedPlanDetailDto> jpdList);
	
	/**
	 * 统计没有成功的任务计划明细
	 * @param planId
	 * @return
	 */
	long countUnSuccessPlanDetails(String planId);
	
	
	/**
	 * 统计没有执行成功的任务计划明细
	 * @param planId
	 * @return
	 */
	long countUnSuccessPlanDetailsByDetailIds(String preDetailId);
	
	public  void planCallBack(String detailId,SuccessFailDealingDto state, String errorMsg);
}
