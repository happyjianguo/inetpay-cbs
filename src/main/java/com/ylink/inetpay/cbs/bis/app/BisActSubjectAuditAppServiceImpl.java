package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisActSubjectAuditService;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.project.cbs.app.BisActSubjectAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActSubjectAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisActSubjectAuditAppService")
public class BisActSubjectAuditAppServiceImpl implements BisActSubjectAuditAppService {

	@Autowired
	BisActSubjectAuditService bisActSubjectAuditService;
	
	@Override
	public BisActSubjectAuditDto findById(String id) throws CbsCheckedException {
		return bisActSubjectAuditService.findById(id);
	}

	@Override
	public PageData<BisActSubjectAuditDto> findPage(PageData<BisActSubjectAuditDto> pageData,
			BisActSubjectAuditDto queryParam) throws CbsCheckedException {
		return bisActSubjectAuditService.findPage(pageData, queryParam);
	}

	@Override
	public void changeActSubject(BisActSubjectAuditDto actSubjectAudit) throws CbsCheckedException {
		bisActSubjectAuditService.changeActSubject(actSubjectAudit);
	}

	@Override
	public long countBySubjectNo(String subjectNo) throws CbsCheckedException {
		return bisActSubjectAuditService.countBySubjectNo(subjectNo);
	}

	@Override
	public void auditPass(String auditor, String auditorName, String id) throws CbsCheckedException {
		bisActSubjectAuditService.auditPass(auditor, auditorName, id);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList) throws CbsCheckedException {
		bisActSubjectAuditService.batchAuditPass(auditor, auditorName, idList);
	}

	@Override
	public void auditReject(String auditor, String auditorName, String id) throws CbsCheckedException {
		bisActSubjectAuditService.auditReject(auditor, auditorName, id);
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName, List<String> idList) throws CbsCheckedException {
		bisActSubjectAuditService.batchAuditReject(auditor, auditorName, idList);
	}

	@Override
	public void cancel(String id) throws CbsCheckedException {
		bisActSubjectAuditService.cancel(id);
	}

	@Override
	public void batchCancel(List<String> idList) throws CbsCheckedException {
		bisActSubjectAuditService.batchCancel(idList);
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditReject) {
		return bisActSubjectAuditService.getAuditNum(ids,auditReject);
	}
}
