/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 */

package com.ylink.inetpay.cbs.bis.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.cache.BisSysParamDtoCache;
import com.ylink.inetpay.cbs.bis.pojo.Result;
import com.ylink.inetpay.cbs.bis.service.BatchSchedulerService;
import com.ylink.inetpay.cbs.bis.service.BisActCustRateAuditService;
import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActBankInterestAppService;
import com.ylink.inetpay.common.project.account.app.ActBookAppService;
import com.ylink.inetpay.common.project.account.app.ActCustInterestAppService;
import com.ylink.inetpay.common.project.account.app.ActFileAppService;
import com.ylink.inetpay.common.project.account.app.ActHistoryAccountAppService;
import com.ylink.inetpay.common.project.cbs.app.BisSmsAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.channel.app.ChlAccountChangeNotifyAppService;
import com.ylink.inetpay.common.project.clear.app.ClearInitAppService;
import com.ylink.inetpay.common.project.clear.app.ClearInnerCheckAppService;
import com.ylink.inetpay.common.project.clear.app.ClearMerchantAppService;
import com.ylink.inetpay.common.project.clear.app.ClearReportAppService;
import com.ylink.inetpay.common.project.clear.app.ClearShareAppService;
import com.ylink.inetpay.common.project.clear.app.ClearTransferFundAppService;
import com.ylink.inetpay.common.project.pay.app.PayFeeSummaryDtoAppService;
import com.ylink.inetpay.common.project.pay.app.PayMerchantAppService;

/**
 * 清结算日终处理
 * 
 * @author LS
 * 
 */
public class DayEndBatchSchedulerServiceImpl extends BatchSchedulerService {

	private static final long serialVersionUID = -4667502953135968975L;

	private static Logger log = LoggerFactory.getLogger(DayEndBatchSchedulerServiceImpl.class);

	@Autowired
	ActBookAppService actBookAppService;
	
	@Autowired
	ClearInitAppService clearInitAppService;
	
	@Autowired
	PayMerchantAppService payMerchantAppService;
	
	@Autowired
	ActFileAppService actFileAppService;

	@Autowired
	ClearInnerCheckAppService clearInnerCheckAppService;
	
	@Autowired
	ClearMerchantAppService clearMerchantAppService;
	
	@Autowired
	ClearShareAppService clearShareAppService;
	
	@Autowired
	ClearTransferFundAppService clearTransferFundAppService;
	
	@Autowired
	ClearReportAppService clearReportAppService;
	@Autowired
	BisSmsAppService bisSmsAppService;
	@Autowired
	ActHistoryAccountAppService actHistoryAccountAppService;
	@Autowired
	ActBankInterestAppService actBankInterestAppService;
	@Autowired
	BisSysParamDtoCache bisSysParamDtoCache;
	@Autowired
	ActCustInterestAppService actCustInterestAppService;
	@Autowired
	private ChlAccountChangeNotifyAppService chlAccountChangeNotifyAppService;
    @Autowired
    private BisExceptionLogService bisExceptionLogService;
    @Autowired
    private BisActCustRateAuditService bisActCustRateAuditService;
    @Autowired
    private PayFeeSummaryDtoAppService payFeeSummaryDtoAppService;
	/**
	 * 批次明细执行
	 */
	@Override
	public Result doBatchBusiness(BisSchedJobQueueDto jq, BisSchedPlanDetailDto detail) {
		Result result = null;
		if (ESchedPlanType.DAY_CUT.equals(detail.getPlanDetailType())) {
			Date now=new Date();
			// 日切
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					try {
						return actBookAppService.dayCut(jq.getExecType(),detail.getAccountDate(),detail.getId());
					} catch (Exception e) {
						SuccessFailDealingDto dto=new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "调用账务系统日切接口出错");
                        bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.DAY_CUT,"调用账务系统日切接口出错"+e.getMessage()));
						log.error("调用账务系统日切接口出错"+e.toString());
						return dto;
					}
				}
			}.execute(jq, detail);
			//执行日切任务作为批处理开始执行时间，如果开始时间为空才会更新进去
			jobPlanService.updateFireTime(detail.getPlanId(),now );
		} else if (ESchedPlanType.TRIAL_BALANCE.equals(detail.getPlanDetailType())) {
			// 试算平衡
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return actBookAppService.trialBalancing(jq.getExecType(),detail.getAccountDate());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.CHECKFILE_INIT.equals(detail.getPlanDetailType())) {
			// 监控记录初始化
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return clearInitAppService.initByAccountDate(detail.getAccountDate());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.PAY_CHECK_PRE.equals(detail.getPlanDetailType())) {
			// 支付系统对账预处理
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return payMerchantAppService.accountFeeAndUploadFile(detail.getAccountDate(), jq.getExecType());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.ACT_CHECK_PRE.equals(detail.getPlanDetailType())) {
			// 账务系统对账预处理
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return actFileAppService.uploadFile(detail.getAccountDate(), jq.getExecType());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.INNER_CHECK.equals(detail.getPlanDetailType())) {
			// 内部对账
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return clearInnerCheckAppService.check(detail.getAccountDate(), jq.getExecType());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.MER_CHECK.equals(detail.getPlanDetailType())) {
			// 接入方对账
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return clearMerchantAppService.uploadMerchantCheckFile(detail.getAccountDate(), jq.getExecType());
				}
			}.execute(jq, detail);
		}/* else if (ESchedPlanType.MER_SETTLE_STATISTICS.equals(detail.getPlanDetailType())) {
			// 商户结算统计
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return clearMerchantAppService.accountToSettle(detail.getAccountDate(), jq.getExecType());
				}
			}.execute(jq, detail);
		}*/
        //取消分润统计
//        else if (ESchedPlanType.PROFIT_STATISTICS.equals(detail.getPlanDetailType())) {
//			// 分润统计
//			result = new TaskCallMethod() {
//				@Override
//				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
//						BisSchedPlanDetailDto detail) throws Exception {
//					return clearShareAppService.accountToShare(detail.getAccountDate(), jq.getExecType());
//				}
//			}.execute(jq, detail);
//		}
//        else if (ESchedPlanType.ACCOUNT_ALLOCATE_STATISTICS.equals(detail.getPlanDetailType())) {
//			// 资金调拨统计
//			result = new TaskCallMethod() {
//				@Override
//				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
//						BisSchedPlanDetailDto detail) throws Exception {
//					return clearTransferFundAppService.accountToTransfer(detail.getAccountDate(), jq.getExecType());
//				}
//			}.execute(jq, detail);
//		} else if (ESchedPlanType.ACCOUNT_ALLOCATE.equals(detail.getPlanDetailType())) {
//			// 资金调拨
//			result = new TaskCallMethod() {
//				@Override
//				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
//						BisSchedPlanDetailDto detail) throws Exception {
//					return clearTransferFundAppService.transfer(detail.getAccountDate(), jq.getExecType());
//				}
//			}.execute(jq, detail);
//		}
      /*  else if (ESchedPlanType.MER_SETTLE.equals(detail.getPlanDetailType())) {
			// 商户结算
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return clearMerchantAppService.settle(detail.getAccountDate(), jq.getExecType());
				}
			}.execute(jq, detail);
		}*/
//        else if (ESchedPlanType.PROFIT.equals(detail.getPlanDetailType())) {
//			// 分润
//			result = new TaskCallMethod() {
//				@Override
//				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
//						BisSchedPlanDetailDto detail) throws Exception {
//					return clearShareAppService.share(detail.getAccountDate(), jq.getExecType());
//				}
//			}.execute(jq, detail);
//		}
       /* else if (ESchedPlanType.REPORT_GEN.equals(detail.getPlanDetailType())) {
			// 报表生成
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					try {
						return clearReportAppService.initReport(detail.getAccountDate(),detail.getId());
					} catch (Exception e) {
						SuccessFailDealingDto dto=new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "调用清结算报表生成接口出错");
						log.error("调用清结算报表生成接口出错"+e.toString());
						return dto;
					}
				}
			}.execute(jq, detail);
		}*/ else if (ESchedPlanType.CUST_CAL_INTEREST.equals(detail.getPlanDetailType())) {
			// 客户计息
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					log.info("执行客户计息");
					return actHistoryAccountAppService.calInterest(EAutoManual.AUTO, detail.getAccountDate(),detail.getId());
				}
			}.execute(jq, detail);
		}else if (ESchedPlanType.CUSTRATE_EFFECTIVE.equals(detail.getPlanDetailType())) {
			// 客户利率生效
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					log.info("客户利率生效");
					return bisActCustRateAuditService.custRateEffective(EAutoManual.AUTO, detail.getAccountDate(),detail.getId());
				}
			}.execute(jq, detail);
		}else if (ESchedPlanType.BANK_CAL_INTEREST.equals(detail.getPlanDetailType())) {
			// 银行计息
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					log.info("银行计息");
					return actBankInterestAppService.settleInterest(EAutoManual.AUTO,detail.getAccountDate(),detail.getId());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.SETTLE_INTEREST_RECORD.equals(detail.getPlanDetailType())) {
			// 生成待清算结息记录
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
                    //将账务日期设置为null重新查询出账务日期
					return actCustInterestAppService.settleInterest(EAutoManual.AUTO, null,detail.getId());
				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.INTEREST_CLEAR.equals(detail.getPlanDetailType())) {
			// 客户利息清算
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					return actCustInterestAppService.interestClear(EAutoManual.AUTO,detail.getAccountDate(),detail.getId());

				}
			}.execute(jq, detail);
		} else if (ESchedPlanType.CHANGE_NEXT_INTEREST_DAY.equals(detail.getPlanDetailType())) {
			// 切换下一结息日期
			result = new TaskCallMethod() {
				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,
						BisSchedPlanDetailDto detail) throws Exception {
					int m=0,d=0;
					try {
						BisSysParamDto month = bisSysParamDtoCache.selectByKey(SystemParamConstants.INTEREST_INTERVAL_MONTH);
						BisSysParamDto day = bisSysParamDtoCache.selectByKey(SystemParamConstants.INTEREST_INTERVAL_DAY);
						if(month == null || day == null){
							return new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "切换下一结息日间隔月份或天数未配置");
						}
						m = Integer.parseInt(month.getValue());
						d = Integer.parseInt(day.getValue());
					} catch (Exception e) {
						log.error("切换下一结息日间隔月份或天数配置错误:{}",ExceptionProcUtil.getExceptionDesc(e));
						return new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "切换下一结息日间隔月份或天数配置错误："+e.getMessage());
					}
					if(m <= 0 && d <=0){
						return new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "切换下一结息日间隔月份或天数配置至少有一个大于0");
					}
					return actBookAppService.updateNextInterestDay(m, d);
				} 
			}.execute(jq, detail);
		} else if(ESchedPlanType.ACCOUNT_CHANGE_FILE_DOWNLOAD.equals(detail.getPlanDetailType())){
			// 动账文件下载预处理
			result = new TaskCallMethod() {

				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq, BisSchedPlanDetailDto detail)
						throws Exception {
					try{
						log.info("动账文件预处理被调度.");
						chlAccountChangeNotifyAppService.accChgFileDownloadTaskInit(detail.getAccountDate());
					}catch(Exception ex){
						log.error("动账文件预处理失败：{}",ExceptionProcUtil.getExceptionDesc(ex));
						return new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "动账文件预处理失败："+ex.getMessage());
					}
					return new SuccessFailDealingDto(ESuccessFailDealing.SUCCESS);
				}
				
			}.execute(jq, detail);
		} else if(ESchedPlanType.FEE_SUMMARY.equals(detail.getPlanDetailType())){
			// 手续费汇总预处理
			result = new TaskCallMethod() {

				@Override
				public SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq, BisSchedPlanDetailDto detail)
						throws Exception {
					try{
						log.info("手续费汇总预处理被调度.");
						payFeeSummaryDtoAppService.feeSummaryTaskInit(detail.getAccountDate());
					}catch(Exception ex){
						log.error("手续费汇总预处理失败：{}",ExceptionProcUtil.getExceptionDesc(ex));
						return new SuccessFailDealingDto(ESuccessFailDealing.FAIL, "手续费汇总预处理失败："+ex.getMessage());
					}
					return new SuccessFailDealingDto(ESuccessFailDealing.SUCCESS);
				}
				
			}.execute(jq, detail);
		}
		return result;
	}
	
	@Override
	public ESchedPlanType getJobTypeKey() {
		return ESchedPlanType.DAY_SCHED_BATCH_DEAL;
	}

	private abstract class TaskCallMethod {
		public Result execute(BisSchedJobQueueDto jq,BisSchedPlanDetailDto detail) {
			boolean hasRelyOn = checkRelyOn(jq, detail);
			if (hasRelyOn)
				return new Result(ESchedJobQueueStatus.PROCESSING,detail.getId(),jq);
			
			try {
				log.debug("执行任务【" + detail.getPlanDetailType().getDisplayName()+ ":" + detail.getId() + "】begin....");
				
				SuccessFailDealingDto sfd = excuteMethod(jq, detail);
				
				log.debug("执行任务【" + detail.getPlanDetailType().getDisplayName() + ":" + detail.getId() + "】end....");
				
				if(sfd == null || sfd.getSfd() == null)
					return new Result(ESchedJobQueueStatus.PROCESSING,detail.getId(),jq,"定时任务返回信息为空");
				
				if(sfd.getSfd() == ESuccessFailDealing.DEALING) 
					return new Result(ESchedJobQueueStatus.PROCESSING,detail.getId(),jq,sfd.getMessage());
				
				if(sfd.getSfd() == ESuccessFailDealing.FAIL)
					return new Result(ESchedJobQueueStatus.FAILURE,detail.getId(),jq,sfd.getMessage());

				return new Result(ESchedJobQueueStatus.SUCCESS,detail.getId(),jq,sfd.getMessage());
			} catch (Exception e) {
				log.error("服务调用-{}异常：", detail.getPlanDetailType().getDisplayName() + ":" + detail.getId(), e);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.TIMER, e.getMessage()));
				//只有明确告知失败，则为失败，否则不设置为失败
				//如果异常就要不就是bug，要不就是超时，置为失败，发送信给运维人
				List<String> list=new ArrayList<String>();
				list.add(EBisTemplateCode.CBS_BATCH_ERROR.getDisplayName());
				bisSmsAppService.sendSms(null, EBisSmsSystem.CBS, list, EBisTemplateCode.CBS_BATCH_ERROR,
						EmessType.MESSAGE_NOTIFICATION);
				return new Result(ESchedJobQueueStatus.FAILURE,detail.getId(),jq,"通讯异常:" + e.getMessage());
			}
		}

		/**
		 * 真正的执行方法
		 */
		public abstract SuccessFailDealingDto excuteMethod(BisSchedJobQueueDto jq,BisSchedPlanDetailDto detail) throws Exception;
	}
}
