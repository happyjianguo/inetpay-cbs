/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-9-4
 */

package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.bis.dao.BisSchedJobQueueDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisSchedPlanDetailDtoMapper;
import com.ylink.inetpay.cbs.bis.pojo.Result;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;

/**
 * @author LS
 * @date 2013-9-4
 * @description：任务计划详情服务
 */
@Service("bisSchedPlanDetailService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSchedPlanDetailServiceImpl implements BisSchedPlanDetailService {

	@Autowired
	private BisSchedPlanDetailDtoMapper bisSchedPlanDetailDtoMapper;
	
	@Autowired
	protected BisSchedPlanService jobPlanService;
	@Autowired
	private BisSchedJobQueueDtoMapper bisSchedJobQueueDtoMapper;
	/**
	 * 计划明细
	 */
	@Override
	public BisSchedPlanDetailDto getJobDetail(String id) {
		if (StringUtils.isEmpty(id))
			return null;
		return bisSchedPlanDetailDtoMapper.selectByPrimaryKey(id);
	}

	/**
	 * 更新明细状态
	 */
	@Override
	public void updateJobPlanDetailStatus(String detailId, ESchedJobQueueStatus status) {
		BisSchedPlanDetailDto detail = getJobDetail(detailId);
		detail.setStatus(status);
		bisSchedPlanDetailDtoMapper.updateByPrimaryKeySelective(detail);
	}

	/**
	 * 更新明细状态
	 */
	@Override
	public void updateJobPlanDetailStatus(String detailId, boolean success, String errMsg) {
		BisSchedPlanDetailDto detail = bisSchedPlanDetailDtoMapper.selectByPrimaryKey(detailId);
		if (ESchedJobQueueStatus.SUCCESS != detail.getStatus()) {
			detail.setStatus(success ? ESchedJobQueueStatus.SUCCESS : ESchedJobQueueStatus.FAILURE);
			detail.setFinishTime(new Date());
			if(StringUtils.isNotBlank(errMsg)) {
				detail.setErrMsg(errMsg);
			}
			detail.setDescription("执行结果[" + (success ? "成功" : "失败") + "]");
			bisSchedPlanDetailDtoMapper.updateByPrimaryKeySelective(detail);
		}
	}

	/**
	 * 更新数据状态
	 */
	@Override
	public void updateDetailStatus(List<Result> results) {
		if (results == null || results.isEmpty())
			return;
		
		Date dt = new Date();
		String planId = null;
		
		for (Result result : results) {
			BisSchedPlanDetailDto detail = bisSchedPlanDetailDtoMapper.selectByPrimaryKey(result.getPlanDetaiId());

				planId = detail.getPlanId();
		
			if (result.getStatus() == ESchedJobQueueStatus.SUCCESS) {
				detail.setFinishTime(dt);
			}
			//实际时间为空时才设置
			if(result.getBisSchedJobQueueDto()!=null&&result.getBisSchedJobQueueDto().getInvokeTime()!=null&&detail.getFireTime()==null)
			{
				detail.setFireTime(result.getBisSchedJobQueueDto().getInvokeTime());
			}
			detail.setErrMsg(result.getMessage());
			if(result.getStatus() == ESchedJobQueueStatus.FAILURE || 
					result.getStatus() == ESchedJobQueueStatus.EXCEPTION) {
				detail.setFailureCount(detail.getFailureCount() + 1);
			}
			detail.setStatus(result.getStatus());
			bisSchedPlanDetailDtoMapper.updateByPrimaryKeySelective(detail);
			//处理中的只能更新新建状态
			if(result.getStatus()==ESchedJobQueueStatus.PROCESSING&&
					detail.getStatus()!=ESchedJobQueueStatus.NEW)
			{
				continue;
			}
			
			jobPlanService.updateJobPlanStatusIfAllDetailFinished(planId, dt);
		}
	}

	/**
	 * 查询依赖未完成任务
	 */
	@Override
	public List<BisSchedPlanDetailDto> listRelyOnUnsuccessful(String jobDetailId) {
		return bisSchedPlanDetailDtoMapper.listRelyOnUnsuccessful(jobDetailId);
	}

	@Override
	public void updateJobDetail(BisSchedPlanDetailDto jobPlanDetail) {
		bisSchedPlanDetailDtoMapper.updateByPrimaryKeySelective(jobPlanDetail);
	}

	/**
	 * 执行计划明细
	 */
	@Override
	public List<BisSchedPlanDetailDto> listJobPlanDetail(String planId) {
		return bisSchedPlanDetailDtoMapper.list(planId);
	}

	@Override
	public int save(BisSchedPlanDetailDto record) {
		return bisSchedPlanDetailDtoMapper.insert(record);
	}

	@Override
	public int saveList(List<BisSchedPlanDetailDto> jpdList) {
		return bisSchedPlanDetailDtoMapper.saveList(jpdList);
	}

	@Override
	public long countUnSuccessPlanDetails(String planId) {
		return bisSchedPlanDetailDtoMapper.countUnSuccessPlanDetails(planId);
	}

	@Override
	public long countUnSuccessPlanDetailsByDetailIds(String preDetailId) {
		if(StringUtils.isBlank(preDetailId))return 0;
		
		String [] pdlIds = preDetailId.split(",");
		List<String> preDetailIdList = new ArrayList<String>();
		for(String pdId : pdlIds) {
			if(StringUtils.isNotBlank(pdId)) {
				preDetailIdList.add(pdId);
			}
		}
		if(preDetailIdList.isEmpty())return 0;
		
		return bisSchedPlanDetailDtoMapper.countUnSuccessPlanDetailsByDetailIds(preDetailIdList);
	}

	@Override
	public void planCallBack(String detailId, SuccessFailDealingDto state,String errorMsg) {
		//暂时只接受失败更新状态
		if(detailId!=null&&state!=null&&state.getSfd()==ESuccessFailDealing.FAIL)
		{
			//查询
			BisSchedPlanDetailDto detail = bisSchedPlanDetailDtoMapper.selectByPrimaryKey(detailId);
			//删除队列
			bisSchedJobQueueDtoMapper.deleteByRefId(detailId);
			//更新状态
			detail.setStatus( ESchedJobQueueStatus.FAILURE);
			detail.setFinishTime(new Date());
			if(StringUtils.isNotBlank(state.getMessage())) {
				detail.setErrMsg(state.getMessage());
			}
			detail.setDescription("执行结果[失败 ]");
			detail.setFailureCount(detail.getFailureCount()+1);
			detail.setErrMsg(errorMsg);
			bisSchedPlanDetailDtoMapper.updateByPrimaryKeySelective(detail);
		}
		
	}
}
