/**
 * 版权所有(C) 2012 深圳市雁联计算系统有限公司
 */

package com.ylink.inetpay.cbs.bis.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.bis.service.BisSchedJobQueueService;
import com.ylink.inetpay.cbs.bis.service.BisSchedPlanService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.constant.EIsFinished;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

/**
 * 管理平台日终处理
 * @author LS
 *
 */
public class DayEndSettlementSchedulerServiceImpl implements SchedulerService{

	private static final long serialVersionUID = -6691493076246167713L;
	
	private static Logger log = LoggerFactory.getLogger(DayEndSettlementSchedulerServiceImpl.class);
	
	private static final String GROUP_ACCOUNT="ACCOUNT";
	private static final String GROUP_PAY="PAY";
	private static final String GROUP_CLEAR="CLEAR";
	
	private static final String GROUP_CHANNEL = "CHANNEL";
	
	@Autowired
	private BisSchedPlanService jobPlanService;
	
	@Autowired
	private BisSchedJobQueueService jobQueueService;
	
	@Autowired
	ActaccountDateService actaccountDateService;
	
	@Override
	public void execute() {
		log.info("管理平台日终处理——初始化");
		initSettlement();
	}

	/**
	 * 检查是否存在未处理数据
	 */
	/*private void checkQueueExists(ESchedPlanType planType){
		List<BisSchedJobQueueDto> list = jobQueueService.list(planType);
		if( list.size() > 0 ) {
			log.error("日终处理异常，至少存在一条待处理任务，不能进行初始化！");
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),
					"日终处理异常，至少存在一条待处理任务，不能进行初始化！");
		}
	}*/
	
	/**
	 * 日终时间
	 * 容错1小时
	 */
	private Calendar getDayEndTime(){
		DateTime dateTime = new DateTime();//当天最后一秒执行
		dateTime = dateTime.minusHours(1)
			.hourOfDay().setCopy(23)
			.minuteOfHour().setCopy(59)
			.secondOfMinute().setCopy(59);
		return dateTime.toCalendar(Locale.getDefault());
	}
	
	/**
	 * 获取账务日期
	 * @return
	 */
	private String getAccountDate() {
		String accountDate = actaccountDateService.getActAccountDateDto().getCurAccountDate();
		if(StringUtils.isBlank(accountDate)) {
			log.error("日终处理异常，获取账务日期失败！");
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"日终处理异常，获取账务日期失败！");
		}
		return accountDate;
	}
	
	/**
	 * 清结算日终
	 */
	protected synchronized void initSettlement(){
		//创建计划
		BisSchedPlanDto plan = createBisSchedPlanDto();
		
		int i=0;
		List<BisSchedPlanDetailDto> details = new ArrayList<BisSchedPlanDetailDto>();
		//日切
		BisSchedPlanDetailDto daycut = createBisSchedPlanDetailDto(plan,  ++i, null, 
				ESchedPlanType.DAY_CUT, "日切", GROUP_ACCOUNT);
		details.add(daycut);
		
		//试算平衡
		BisSchedPlanDetailDto trialBalance = createBisSchedPlanDetailDto(plan, ++i, 
				daycut.getId(), ESchedPlanType.TRIAL_BALANCE, "试算平衡",GROUP_ACCOUNT);
		details.add(trialBalance);
		
		//切换下一个结息日期
		BisSchedPlanDetailDto changeNextInterestDay = createBisSchedPlanDetailDto(plan, ++i, 
						trialBalance.getId(), ESchedPlanType.CHANGE_NEXT_INTEREST_DAY, "切换下一个结息日",GROUP_ACCOUNT);
		details.add(changeNextInterestDay);
		
		//客户计息
		BisSchedPlanDetailDto custCalInterest = createBisSchedPlanDetailDto(plan, ++i, 
				changeNextInterestDay.getId(), ESchedPlanType.CUST_CAL_INTEREST, "客户计息",GROUP_ACCOUNT);
		details.add(custCalInterest);
		//客户利率生效
		BisSchedPlanDetailDto custRateEffective = createBisSchedPlanDetailDto(plan, ++i, 
				custCalInterest.getId(), ESchedPlanType.CUSTRATE_EFFECTIVE, "客户利率生效",GROUP_ACCOUNT);
		details.add(custRateEffective);
		//银行计息
		BisSchedPlanDetailDto bankCalInterest = createBisSchedPlanDetailDto(plan, ++i, 
				changeNextInterestDay.getId(), ESchedPlanType.BANK_CAL_INTEREST, "银行计息",GROUP_ACCOUNT);
		details.add(bankCalInterest);
		
		//生成待清算结息记录
		BisSchedPlanDetailDto settleInterestRecord = createBisSchedPlanDetailDto(plan, ++i, 
				custCalInterest.getId(), ESchedPlanType.SETTLE_INTEREST_RECORD, "生成待清算结息记录",GROUP_ACCOUNT);
		details.add(settleInterestRecord);
		
		//客户利息清算
		BisSchedPlanDetailDto interestClear = createBisSchedPlanDetailDto(plan, ++i, 
				settleInterestRecord.getId(), ESchedPlanType.INTEREST_CLEAR, "客户利息清算",GROUP_ACCOUNT);
		details.add(interestClear);
		
		//监控记录初始化
		BisSchedPlanDetailDto checkFileInit = createBisSchedPlanDetailDto(plan, ++i, 
				trialBalance.getId(), ESchedPlanType.CHECKFILE_INIT, "监控记录初始化",GROUP_CLEAR);
		details.add(checkFileInit);
		
		//支付系统对账预处理
		BisSchedPlanDetailDto payCheckPre = createBisSchedPlanDetailDto(plan, ++i, 
				checkFileInit.getId(), ESchedPlanType.PAY_CHECK_PRE, "支付系统对账预处理",GROUP_PAY);
		details.add(payCheckPre);
		
		//账务系统对账预处理
		BisSchedPlanDetailDto actCheckPre = createBisSchedPlanDetailDto(plan, ++i, 
				checkFileInit.getId(), ESchedPlanType.ACT_CHECK_PRE, "账务系统对账预处理",GROUP_ACCOUNT);
		details.add(actCheckPre);
		
		//内部对账
		BisSchedPlanDetailDto innerCheck = createBisSchedPlanDetailDto(plan, ++i, 
				payCheckPre.getId()+","+actCheckPre.getId(), ESchedPlanType.INNER_CHECK, "内部对账",GROUP_CLEAR);
		details.add(innerCheck);
		
		//接入方对账
		BisSchedPlanDetailDto merCheck = createBisSchedPlanDetailDto(plan, ++i, 
				innerCheck.getId(), ESchedPlanType.MER_CHECK, "接入方对账",GROUP_CLEAR);
		details.add(merCheck);
		
		//商户结算统计
//		BisSchedPlanDetailDto merSettleStatistics = createBisSchedPlanDetailDto(plan, ++i,
//				innerCheck.getId(), ESchedPlanType.MER_SETTLE_STATISTICS, "商户结算统计",GROUP_CLEAR);
//		details.add(merSettleStatistics);

//		//分润统计
//		BisSchedPlanDetailDto profitStatistics = createBisSchedPlanDetailDto(plan, ++i,
//				innerCheck.getId(), ESchedPlanType.PROFIT_STATISTICS, "分润统计",GROUP_CLEAR);
//		details.add(profitStatistics);
//
//		//资金调拨统计
//		BisSchedPlanDetailDto accountAllocateStatistics = createBisSchedPlanDetailDto(plan, ++i,
//				merSettleStatistics.getId()+","+profitStatistics.getId(), ESchedPlanType.ACCOUNT_ALLOCATE_STATISTICS, "资金调拨统计",GROUP_CLEAR);
//		details.add(accountAllocateStatistics);
//
//		//资金调拨
//		BisSchedPlanDetailDto accountAllocate = createBisSchedPlanDetailDto(plan, ++i,
//				accountAllocateStatistics.getId(), ESchedPlanType.ACCOUNT_ALLOCATE, "资金调拨",GROUP_CLEAR);
//		details.add(accountAllocate);
		
		//商户结算
//		BisSchedPlanDetailDto merSettle = createBisSchedPlanDetailDto(plan, ++i,
//                merSettleStatistics.getId(), ESchedPlanType.MER_SETTLE, "商户结算",GROUP_CLEAR);
//		details.add(merSettle);
		
//		//分润
//		BisSchedPlanDetailDto profit = createBisSchedPlanDetailDto(plan, ++i,
//				merSettle.getId(), ESchedPlanType.PROFIT, "分润",GROUP_CLEAR);
//		details.add(profit);
		
		//报表生成
/*		BisSchedPlanDetailDto reportGen = createBisSchedPlanDetailDto(plan, ++i,
                innerCheck.getId(), ESchedPlanType.REPORT_GEN, "报表生成",GROUP_CLEAR);
		details.add(reportGen);*/
		
		//动账补发文件处理
		BisSchedPlanDetailDto accChgFileDownload = createBisSchedPlanDetailDto(plan, ++i,
                innerCheck.getId(), ESchedPlanType.ACCOUNT_CHANGE_FILE_DOWNLOAD, "动账补发文件下载预处理",GROUP_CHANNEL);
		details.add(accChgFileDownload);
		
		// 手续费汇总处理
		BisSchedPlanDetailDto feeSummary = createBisSchedPlanDetailDto(plan, ++i,
                innerCheck.getId(), ESchedPlanType.FEE_SUMMARY, "手续费汇总预处理",GROUP_PAY);
		details.add(feeSummary);
		
		jobPlanService.save(plan,details);
	}
	
	private BisSchedPlanDto createBisSchedPlanDto(){
		String accountDate = getAccountDate();
		
		String planid = ESchedPlanType.DAY_SCHED_BATCH_DEAL.getValue() + accountDate;
		
		BisSchedPlanDto plan = new BisSchedPlanDto();
		plan.setId(planid);
		plan.setPlanType(ESchedPlanType.DAY_SCHED_BATCH_DEAL);
		plan.setAccountDate(accountDate);
		plan.setFinished(EIsFinished.UNFINISHED);
		plan.setPlanFireTime(getDayEndTime().getTime());
		plan.setDescription("日终处理");
		plan.setCreateTime(new Date());
		return plan;
	}
	
	private BisSchedPlanDetailDto createBisSchedPlanDetailDto(BisSchedPlanDto plan,
			int seq,String preDetailId,ESchedPlanType planDetailType,String description,String group){
		BisSchedPlanDetailDto planDetailDto = new BisSchedPlanDetailDto();
		planDetailDto.setId(plan.getId() + "-" + seq);
		planDetailDto.setPlanId(plan.getId());
		planDetailDto.setPlanType(plan.getPlanType());
		planDetailDto.setPlanDetailType(planDetailType);
		planDetailDto.setPreDetailId(preDetailId);
		planDetailDto.setStatus(ESchedJobQueueStatus.NEW);
		planDetailDto.setErrMsg("");
		planDetailDto.setFailureCount(0);
		planDetailDto.setFinished(EIsFinished.UNFINISHED);
		planDetailDto.setGroupName(group);
		planDetailDto.setAccountDate(plan.getAccountDate());
		planDetailDto.setDescription(description);
		planDetailDto.setCreateTime(new Date());
		planDetailDto.setBusiUrl(planDetailType.getBusiUrl());
		return planDetailDto;
	}
}
