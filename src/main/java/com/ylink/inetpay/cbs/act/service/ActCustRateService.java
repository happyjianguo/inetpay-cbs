package com.ylink.inetpay.cbs.act.service;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;

public interface ActCustRateService {

	ActCustRateDto findById(String id);

	PageData<ActCustRateDto> findPage(PageData<ActCustRateDto> pageData, ActCustRateDto queryParam);
	
	PageData<ActCustRateDto> findCustRatePage(PageData<ActCustRateDto> pageDate, ActCustRateDto actCustRateDto);
	
	ActCustRateDto findByAccountId(String accountId);
	
	ActCustRateDto findByBankCardNo(String bankCardNo);
	/**
	 * 获取客户利率集合
	 * @return
	 */
	Map<String, ActCustRateDto> findCustRateMap(List<String> accountIds);
}
