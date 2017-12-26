package com.ylink.inetpay.cbs.bis.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.pojo.Result;
import com.ylink.inetpay.cbs.bis.pojo.TriggerWrap;
import com.ylink.inetpay.cbs.bis.service.BisSchedPlanDetailService;
import com.ylink.inetpay.cbs.bis.service.BisSchedPlanService;
import com.ylink.inetpay.cbs.bis.service.SchedulerManager;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.app.BisSchedulerAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.TriggerWrapDTO;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisSchedulerAppService")
public class BisSchedulerAppServiceImpl implements BisSchedulerAppService {
	private static Logger log = LoggerFactory.getLogger(BisSchedulerAppServiceImpl.class);
	@Autowired
	private SchedulerManager schedulerManager;

	@Autowired
	private BisSchedPlanService jobPlanService;
	
	@Autowired
	private BisSchedPlanDetailService jobPlanDetailService;

	/**
	 * 转换
	 * 
	 * @param triggerWrap
	 * @return
	 */
	private TriggerWrapDTO convert(TriggerWrap triggerWrap) {
		TriggerWrapDTO twd = new TriggerWrapDTO();
		twd.setCronExpression(triggerWrap.getCronExpression());
		twd.setDescription(triggerWrap.getDescription());
		twd.setEndTime(triggerWrap.getEndTime());
		twd.setGroup(triggerWrap.getGroup());
		twd.setName(triggerWrap.getName());
		twd.setNextFireTime(triggerWrap.getNextFireTime());
		twd.setPreviousFireTime(triggerWrap.getPreviousFireTime());
		twd.setPause(triggerWrap.isPause());
		return twd;
	}

	@Override
	public List<TriggerWrapDTO> listAllTrigger() throws CbsCheckedException {
		List<TriggerWrap> items = schedulerManager.listAllTrigger();

		List<TriggerWrapDTO> twdList = new ArrayList<TriggerWrapDTO>();

		if (items != null && !items.isEmpty()) {
			for (TriggerWrap triggerWrap : items) {
				twdList.add(convert(triggerWrap));
			}
		}
		return twdList;
	}

	@Override
	public TriggerWrapDTO get(String group, String triggerName)
			throws CbsCheckedException {
		return convert(schedulerManager.get(group, triggerName));
	}

	@Override
	public void updateCronExp(String name, String group, String cronExp)
			throws CbsCheckedException {
		schedulerManager.updateCronExp(name, group, cronExp);
	}

	@Override
	public void toggleTrigger(String triggerName, String group)
			throws CbsCheckedException {
		schedulerManager.toggleTrigger(triggerName,group);
	}

	@Override
	public void refresh(String name, String group) throws CbsCheckedException {
		schedulerManager.refresh(name, group);
	}

	@Override
	public PageData<BisSchedPlanDto> listJobPlanPage(
			PageData<BisSchedPlanDto> pageData, BisSchedPlanDto queryParam)
			throws CbsCheckedException {
		return jobPlanService.listJobPlanPage(pageData, queryParam);
	}

	@Override
	public List<BisSchedPlanDetailDto> listJobPlanDetail(String planId)
			throws CbsCheckedException {
		return jobPlanDetailService.listJobPlanDetail(planId);
	}

	@Override
	public void detailInQueue(String jobDetailId) throws CbsCheckedException {
		jobPlanService.detailInQueue(jobDetailId,EAutoManual.MANUAL);
	}

	@Override
	public BisSchedPlanDetailDto getJobDetail(String id)
			throws CbsCheckedException {
		return jobPlanDetailService.getJobDetail(id);
	}

	@Override
	public long countUnSuccessPlanDetailsByDetailIds(String preDetailId)
			throws CbsCheckedException {
		return jobPlanDetailService.countUnSuccessPlanDetailsByDetailIds(preDetailId);
	}

	@Override
	public  void planCallBack(String detailId,SuccessFailDealingDto state, String errorMsg)
	{
		jobPlanDetailService.planCallBack(detailId, state,errorMsg);
	}
	@Override
	public void planRetry(String accountDate,ESchedPlanType  type)
	{
		String  planDetaiId="DSBD"+accountDate;
		switch (type) {
		case ACCOUNT_ALLOCATE:
			planDetaiId+="-10";
			break;
		case MER_SETTLE:
			planDetaiId+="-11";
			break;
		case PROFIT:
			planDetaiId+="-12";
			break;
		default:
			log.error("回调类型错误："+type);
			return;
		}
		BisSchedJobQueueDto schedJobQueueDto=new BisSchedJobQueueDto();
		schedJobQueueDto.setInvokeTime(new Date());
		Result result=new Result(ESchedJobQueueStatus.SUCCESS, planDetaiId, schedJobQueueDto);
		List<Result> results=new ArrayList<Result>();
		results.add(result);
		jobPlanDetailService.updateDetailStatus(results);
		jobPlanService.executeRelyOnJobPlanDetail(planDetaiId,EAutoManual.AUTO);
		
	}
}
