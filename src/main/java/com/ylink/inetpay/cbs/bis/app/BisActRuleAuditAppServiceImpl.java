package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisActRuleAuditService;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActRuleType;
import com.ylink.inetpay.common.core.constant.ETradeType;
import com.ylink.inetpay.common.project.cbs.app.BisActRuleAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActRuleAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisActRuleAuditAppService")
public class BisActRuleAuditAppServiceImpl implements BisActRuleAuditAppService {

	@Autowired
	BisActRuleAuditService bisActRuleAuditService;
	
	@Override
	public PageData<BisActRuleAuditDto> findPage(PageData<BisActRuleAuditDto> pageData, BisActRuleAuditDto queryParam)
			throws CbsCheckedException {
		return bisActRuleAuditService.findPage(pageData, queryParam);
	}

	@Override
	public void changeActRule(BisActRuleAuditDto actRuleAudit) throws CbsCheckedException {
		bisActRuleAuditService.changeActRule(actRuleAudit);
	}

	@Override
	public void auditPass(String auditor, String auditorName, String id) throws CbsCheckedException {
		bisActRuleAuditService.auditPass(auditor, auditorName, id);
	}

	@Override
	public void auditReject(String auditor, String auditorName, String id) throws CbsCheckedException {
		bisActRuleAuditService.auditReject(auditor, auditorName, id);
	}

	@Override
	public void cancel(String id) throws CbsCheckedException {
		bisActRuleAuditService.cancel(id);
	}

	@Override
	public long countWaitAuditByTradeTypeAndRuleType(ETradeType tradeType, EActRuleType ruleType)
			throws CbsCheckedException {
		return bisActRuleAuditService.countWaitAuditByTradeTypeAndRuleType(tradeType, ruleType);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList) throws CbsCheckedException {
		bisActRuleAuditService.batchAuditPass(auditor, auditorName, idList);
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName, List<String> idList) throws CbsCheckedException {
		bisActRuleAuditService.batchAuditReject(auditor, auditorName, idList);
	}

	@Override
	public void batchCancel(List<String> idList) throws CbsCheckedException {
		bisActRuleAuditService.batchCancel(idList);
	}

	@Override
	public BisActRuleAuditDto findById(String id) throws CbsCheckedException {
		return bisActRuleAuditService.findById(id);
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass) {
		return bisActRuleAuditService.getAuditNum(ids,auditPass);
	}
}
