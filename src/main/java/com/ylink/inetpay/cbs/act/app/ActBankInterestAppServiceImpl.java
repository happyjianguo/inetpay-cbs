package com.ylink.inetpay.cbs.act.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActBankInterestService;
import com.ylink.inetpay.common.project.account.dto.ActBankInterestDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActBankInterestAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("cbsActBankInterestAppService")
public class ActBankInterestAppServiceImpl implements CbsActBankInterestAppService {

	@Autowired
	ActBankInterestService actBankInterestService;
	
	@Override
	public PageData<ActBankInterestDto> findPage(PageData<ActBankInterestDto> pageData, ActBankInterestDto queryParam)
			throws CbsCheckedException {
		return actBankInterestService.findPage(pageData, queryParam);
	}

	@Override
	public ActBankInterestDto findById(String id) throws CbsCheckedException {
		return actBankInterestService.findById(id);
	}

}
