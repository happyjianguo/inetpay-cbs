package com.ylink.inetpay.cbs.cls.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsCallAcctService;
import com.ylink.inetpay.common.project.cbs.app.ClsCallAcctAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.clear.dto.ClsCallAcct;
@Service("clsCallAcctAppService")
public class ClsCallAcctAppServiceImpl implements ClsCallAcctAppService {
	@Autowired
	private ClsCallAcctService clsCallAcctService;
	@Override
	public PageData<ClsCallAcct> pageAuditPageList(ClsCallAcct clsCallAcct,
			PageData<ClsCallAcct> pageData) throws CbsCheckedException {
		return clsCallAcctService.pageAuditPageList(clsCallAcct, pageData);
	}

	@Override
	public PageData<ClsCallAcct> pageAuditResultPageList(
			ClsCallAcct clsCallAcct, PageData<ClsCallAcct> pageData)
			throws CbsCheckedException {
		return clsCallAcctService.pageAuditResultPageList(clsCallAcct, pageData);
	}

	@Override
	public PageData<ClsCallAcct> pagePayPageList(ClsCallAcct clsCallAcct,
			PageData<ClsCallAcct> pageData) throws CbsCheckedException {
		return clsCallAcctService.pagePayPageList(clsCallAcct, pageData);
	}

	@Override
	public ClsCallAcct details(String id) throws CbsCheckedException {
		return clsCallAcctService.details(id);
	}

	@Override
	public void auditPass(ClsCallAcct acctDto)throws CbsCheckedException {
		clsCallAcctService.auditPass(acctDto);
	}

	@Override
	public void againPay(String id)throws CbsCheckedException {
		clsCallAcctService.againPay(id);
	}

	@Override
	public boolean callAcctSuccessful(String callDay)
			throws CbsCheckedException {
		return clsCallAcctService.callAcctSuccessful(callDay);
	}
}
