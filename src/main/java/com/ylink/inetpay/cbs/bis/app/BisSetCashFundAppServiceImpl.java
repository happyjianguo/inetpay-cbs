package com.ylink.inetpay.cbs.bis.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisSetCashFundService;
import com.ylink.inetpay.common.core.constant.AuditTypeEnum;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EPocessStatusEnum;
import com.ylink.inetpay.common.core.constant.EffectiveStatusEnum;
import com.ylink.inetpay.common.project.cbs.app.BisSetCashFundAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundOperDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisSetCashFundAppService")
public class BisSetCashFundAppServiceImpl implements BisSetCashFundAppService {
	@Autowired
	private BisSetCashFundService bisSetCashFundService;
	@Override
	public PageData<BisSetCashfund> findCheckStatus(PageData<BisSetCashfund> pageData, BisSetCashfund queryParam) {
		return bisSetCashFundService.findCheckStatus(pageData, queryParam);
	}

	@Override
	public BisSetCashfund getView(String id) {
		return bisSetCashFundService.getView(id);
	}

	@Override
	public List<String> batchAudit(List<String> ids, UcsSecUserDto userDto, AuditTypeEnum auditType,String auditReason)throws CbsCheckedException {
		//审核通过时，终止现在已经在跑批次的数据。
		if(AuditTypeEnum.AUDIT_PASS==auditType){
			bisSetCashFundService.stopSetCashFund(ids,EPocessStatusEnum.STOP);
		}
		return bisSetCashFundService.batchAudit(ids, userDto, auditType,auditReason);
	}

	@Override
	public List<String> batchSetCashFund(List<String> balanceAccountIds,List<String> cashfundAccountIds, UcsSecUserDto userDto,Long setAmount,String effectiveDate,String remarks) throws CbsCheckedException {
		return bisSetCashFundService.batchSetCashFund(balanceAccountIds,cashfundAccountIds, userDto, setAmount, effectiveDate,remarks);
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass, EffectiveStatusEnum waitEffect) {
		return bisSetCashFundService.getAuditNum(ids,auditPass,waitEffect);
	}

	@Override
	public List<BisSetCashFundOperDto> getOperList(String id) {
		return bisSetCashFundService.getOperList(id);
	}
	
	/**
	 * 根据批次号获取保证金map
	 */
	@Override
	public Map<String, BisSetCashfund> findCashFundMapByBatchNo(String batchNo) {
		return bisSetCashFundService.findCashFundMapByBatchNo(batchNo);
	}

	@Override
	public List<BisSetCashfund> list(BisSetCashfund queryParam) {
		return bisSetCashFundService.list(queryParam);
	}

	@Override
	public Map<String,BisSetCashfund> findExistWaitAuditMap(List<String> accountIds) {
		return bisSetCashFundService.findExistWaitAuditMap(accountIds);
	}
	
}
