package com.ylink.inetpay.cbs.act.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActCustRateService;
import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActCustRateAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("cbsActCustRateAppService")
public class ActCustRateAppServiceImpl implements CbsActCustRateAppService {

	@Autowired
	ActCustRateService actCustRateService;
	
	@Override
	public PageData<ActCustRateDto> findPage(PageData<ActCustRateDto> pageDate, ActCustRateDto actCustRateDto)
			throws CbsCheckedException {
		return actCustRateService.findPage(pageDate, actCustRateDto);
	}

	@Override
	public ActCustRateDto findById(String id) throws CbsCheckedException {
		return actCustRateService.findById(id);
	}

	@Override
	public ActCustRateDto findByAccountId(String accountId) throws CbsCheckedException {
		return actCustRateService.findByAccountId(accountId);
	}

	@Override
	public ActCustRateDto findByBankCardNo(String bankCardNo) throws CbsCheckedException {
		return actCustRateService.findByBankCardNo(bankCardNo);
	}

	@Override
	public PageData<ActCustRateDto> findCustRatePage(PageData<ActCustRateDto> pageDate, ActCustRateDto actCustRateDto)
			throws CbsCheckedException {
		return actCustRateService.findCustRatePage(pageDate, actCustRateDto);
	}

	@Override
	public Map<String, ActCustRateDto> findCustRateMap(List<String> accountIds) {
		return actCustRateService.findCustRateMap(accountIds);
	}

}
