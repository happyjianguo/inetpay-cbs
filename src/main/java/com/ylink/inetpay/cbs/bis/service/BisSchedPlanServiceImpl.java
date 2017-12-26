package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisSchedPlanDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EIsFinished;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDto;

@Service("bisSchedPlanService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSchedPlanServiceImpl implements BisSchedPlanService {

	@Autowired
	private BisSchedJobQueueService jobQueueService;

	@Autowired
	private BisSchedPlanDetailService jobPlanDetailService;

	@Autowired
	private BisSchedPlanDtoMapper bisSchedPlanDtoMapper;

	@Override
	public void updateJobPlanStatusIfAllDetailFinished(String jobPlanId , Date finishTime) {
		long count = jobPlanDetailService.countUnSuccessPlanDetails(jobPlanId);
		if(count == 0) {
			BisSchedPlanDto planDto = bisSchedPlanDtoMapper.selectByPrimaryKey(jobPlanId);
			planDto.setFinished(EIsFinished.FINISHED);
			planDto.setFinishTime(new Date());
			bisSchedPlanDtoMapper.updateByPrimaryKeySelective(planDto);
		}
	}

	@Override
	public void executeRelyOnJobPlanDetail(String detailId,EAutoManual execType) {
		// 执行依赖任务
		List<BisSchedPlanDetailDto> replyOnList = jobPlanDetailService.listRelyOnUnsuccessful(detailId);
		if (replyOnList == null || replyOnList.isEmpty())
			return;
		
		for (BisSchedPlanDetailDto item : replyOnList) {
			if(StringUtils.isNotBlank(item.getPreDetailId())) {
				long count = jobPlanDetailService.countUnSuccessPlanDetailsByDetailIds(item.getPreDetailId());
				if(count == 0) {
					//依赖的任务已经全部执行成功，则将该任务加入执行队列
					jobQueueService.jobInQueue(item.getPlanType(),item.getId(), new Date(),execType);
				}
			} else {
				jobQueueService.jobInQueue(item.getPlanType(),item.getId(), new Date(),execType);
			}
		}
	}

	/**
	 * 保存执行计划
	 */
	@Override
	public void save(BisSchedPlanDto plan, List<BisSchedPlanDetailDto> details) {
		// 保存计划
		if (bisSchedPlanDtoMapper.selectByPrimaryKey(plan.getId()) != null) {
			return ;
		}
		bisSchedPlanDtoMapper.insert(plan);
		
		// 保存计划详情
		List<String> planDetailIds = new ArrayList<String>();
		for (BisSchedPlanDetailDto item : details) {
			// 只有没有依赖的任务才进入执行队列
			if(StringUtils.isBlank(item.getPreDetailId())) {
				item.setStatus(ESchedJobQueueStatus.PROCESSING);//进入队列的处理中状态
				planDetailIds.add(item.getId());
			}
		}
		
		if (!details.isEmpty()) {
			jobPlanDetailService.saveList(details);
		}
		jobQueueService.jobInQueue(plan.getPlanType(),planDetailIds.toArray(new String[0]), new Date(),EAutoManual.AUTO);
	}

	/**
	 * 明细入任务队列
	 */
	@Override
	public void detailInQueue(BisSchedPlanDetailDto pd,EAutoManual execType) {
		detailInQueue(pd.getId(),execType);
	}

	/**
	 * 明细入任务队列
	 */
	@Override
	public void detailInQueue(String jobDetailId,EAutoManual execType) {
		BisSchedPlanDetailDto detail = jobPlanDetailService.getJobDetail(jobDetailId);
		jobQueueService.jobInQueue(detail.getPlanType(),detail.getId(), new Date(),execType);
		//同时将该计划明细状态该为新建状态
		detail.setStatus(ESchedJobQueueStatus.PROCESSING);
		detail.setErrMsg("");
		jobPlanDetailService.updateJobDetail(detail);
	}

	/**
	 * 更新执行时间
	 */
	@Override
	public void updateFireTime(String id, Date fireTime) {
		if (fireTime == null)
			fireTime = new Date();

		BisSchedPlanDto plan = bisSchedPlanDtoMapper.selectByPrimaryKey(id);
		//开始执行时间以第一次执行时间为准，如果不为空，不修改
		if (plan != null&&plan.getFireTime()==null) {
			plan.setFireTime(fireTime);
			bisSchedPlanDtoMapper.updateByPrimaryKeySelective(plan);
		}
	}

	/**
	 * 更新完成时间
	 */
	@Override
	public void updateFinishTime(String id, Date finishTime) {
		if (StringUtils.isEmpty(id))
			return;

		if (finishTime == null)
			finishTime = new Date();

		BisSchedPlanDto plan = bisSchedPlanDtoMapper.selectByPrimaryKey(id);
		if (plan != null) {
			if (plan.getFinishTime() == null)
				plan.setFinishTime(finishTime);
			bisSchedPlanDtoMapper.updateByPrimaryKeySelective(plan);
		}
	}

	@Override
	public PageData<BisSchedPlanDto> listJobPlanPage(PageData<BisSchedPlanDto> pageData,
			BisSchedPlanDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisSchedPlanDto> items = bisSchedPlanDtoMapper.listJobPlan(queryParam.getId(), 
				queryParam.getAccountDate());
		
		Page<BisSchedPlanDto> page = (Page<BisSchedPlanDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}
}
