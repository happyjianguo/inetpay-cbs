package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisBatchExpService;
import com.ylink.inetpay.cbs.bis.service.BisSetCashFundService;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EBatchBusiType;
import com.ylink.inetpay.common.core.constant.EPocessStatusEnum;
import com.ylink.inetpay.common.project.cbs.app.BisBatchExpAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBatchExp;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisBatchExpAppService")
public class BisBatchExpAppServiceImpl implements BisBatchExpAppService {
	@Autowired
	private BisBatchExpService bisBatchExpService;
	@Autowired
	private BisSetCashFundService bisSetCashFundService;
	private static Logger _log = LoggerFactory.getLogger(BisBatchExpAppServiceImpl.class);
	@Override
	public PageData<BisBatchExp> findPageList(PageData<BisBatchExp> pageData, BisBatchExp queryParam) {
		return bisBatchExpService.findPageList(pageData, queryParam);
	}
	@Override
	public long batchCashFundExp(List<BisSetCashfund> batchDtos, BisBatchExp bisBatchExp) throws CbsCheckedException {
		return bisBatchExpService.batchCashFundExp(batchDtos, bisBatchExp);
	}
	@Override
	public long batchAccountFrozenExp(List<BisAccountFrozenAuditDto> batchDtos, BisBatchExp bisBatchExp)
			throws CbsCheckedException {
		return bisBatchExpService.batchAccountFrozenExp(batchDtos, bisBatchExp);
	}
	@Override
	public long batchCustRateExp(List<BisActCustRateAuditDto> batchDtos, BisBatchExp bisBatchExp)
			throws CbsCheckedException {
		return bisBatchExpService.batchCustRateExp(batchDtos, bisBatchExp);
	}
	@Override
	public long batchEcternalPayExp(List<BisEcternalPayments> batchDtos, BisBatchExp bisBatchExp)
			throws CbsCheckedException {
		return bisBatchExpService.batchEcternalPayExp(batchDtos, bisBatchExp);
	}
	@Override
	public BisBatchExp findListByBatchNo(String batchNo) {
		return bisBatchExpService.findListByBatchNo(batchNo);
	}
	
	@Override
	public long batchExpAudit(String batchNo, BISAuditStatus auditStatus,EBatchBusiType busiType,String loginName,String realName,String checkReason)throws CbsCheckedException {
		_log.info("批次号【"+batchNo+"】业务类型【"+busiType.getDisplayName()+"】复核通过开始");
		//如果是批次保证金复核通过，中断正在执行的保证金设置
		if(EBatchBusiType.BATCH_CASHFUND==busiType){
			bisSetCashFundService.stopSetCashFundByBatchNo(batchNo, EPocessStatusEnum.STOP);
		}
		return bisBatchExpService.batchExpAudit(batchNo,auditStatus,busiType,loginName,realName,checkReason);
	}
	@Override
	public String getBatchNo() {
		return bisBatchExpService.getBatchNo();
	}
	@Override
	public boolean isExistFile(String expFileName, EBatchBusiType busiType) {
		return bisBatchExpService.isExistFile(expFileName,busiType);
	}
	
	/*public void batchFrozenExpAudit(BISAuditStatus auditStatus, final String batchNo){
		bisAccountFrozenAuditService.updateBatchAuditStatus(auditStatus,batchNo);
		//如果影响行数不小于0，且复核通过需要调用支付接口实行冻结（复核通过只修改状态，由自动冻结定时任务执行冻结）
		if(updateNum>0 && BISAuditStatus.WAIT_AUDIT!=auditStatus && BISAuditStatus.AUDIT_REJECT!=auditStatus){
			new Thread(new Runnable() {
				public void run() {
					List<BisAccountFrozenAuditDto> accountFrozenAuditDtos=bisAccountFrozenAuditService.findListByBatchNo(batchNo);
					if(accountFrozenAuditDtos==null || accountFrozenAuditDtos.isEmpty()){
						return;
					}
					for (final BisAccountFrozenAuditDto dto : accountFrozenAuditDtos) {
						new Thread(new Runnable() {
							public void run() {
								//将状态修改为处理中
								long num=bisAccountFrozenAuditService.updateDealStatus(EActInterestDealStatus.DEALING,EActInterestDealStatus.WAIT_DEAL,dto.getId());
								if(num>0){
									bisAccountFrozenAuditService.batchExpAudit(dto);
								}else {
									_log.info("记录【"+dto.getId()+"】处理中，不可以冻结");
								}	
							}
						}).start();
					}	
				}
			}).start();
		}
	}*/
}
