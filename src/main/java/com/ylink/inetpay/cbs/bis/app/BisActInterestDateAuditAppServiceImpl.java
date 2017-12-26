package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisActInterestDateAuditService;
import com.ylink.inetpay.common.project.cbs.app.BisActInterestDateAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActInterestDateAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisActInterestDateAuditAppService")
public class BisActInterestDateAuditAppServiceImpl implements BisActInterestDateAuditAppService {

	@Autowired
	BisActInterestDateAuditService bisActInterestDateAuditService;
	
	@Override
	public BisActInterestDateAuditDto findById(String id) throws CbsCheckedException {
		return bisActInterestDateAuditService.findById(id);
	}

	@Override
	public PageData<BisActInterestDateAuditDto> findPage(PageData<BisActInterestDateAuditDto> pageData,
			BisActInterestDateAuditDto queryParam) throws CbsCheckedException {
		return bisActInterestDateAuditService.findPage(pageData, queryParam);
	}

	@Override
	public void save(BisActInterestDateAuditDto actCustRateAudit) throws CbsCheckedException {
		bisActInterestDateAuditService.save(actCustRateAudit);
	}

	@Override
	public void auditPass(String auditor, String auditorName, String id) throws CbsCheckedException {
		bisActInterestDateAuditService.auditPass(auditor, auditorName, id);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList) throws CbsCheckedException {
		bisActInterestDateAuditService.batchAuditPass(auditor, auditorName, idList);
	}

	@Override
	public void auditReject(String auditor, String auditorName, String reason, String id) throws CbsCheckedException {
		bisActInterestDateAuditService.auditReject(auditor, auditorName, reason, id);
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName, String reason, List<String> idList)
			throws CbsCheckedException {
		bisActInterestDateAuditService.batchAuditReject(auditor, auditorName,reason, idList);
	}

	@Override
	public void cancel(String loginName,String realName,String id, String reason) throws CbsCheckedException {
		bisActInterestDateAuditService.cancel(loginName,realName,id, reason);
	}

	@Override
	public void batchCancel(String loginName,String realName,List<String> idList, String reason) throws CbsCheckedException {
		bisActInterestDateAuditService.batchCancel(loginName,realName,idList, reason);
	}

	@Override
	public long countWaitAuditWithAccountId(String accountId) throws CbsCheckedException {
		return bisActInterestDateAuditService.countWaitAuditWithAccountId(accountId);
	}

}
