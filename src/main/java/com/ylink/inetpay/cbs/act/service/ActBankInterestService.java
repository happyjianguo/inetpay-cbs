package com.ylink.inetpay.cbs.act.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActBankInterestDto;

public interface ActBankInterestService {

	PageData<ActBankInterestDto> findPage(PageData<ActBankInterestDto> pageData,
			ActBankInterestDto queryParam) ;
	
	ActBankInterestDto findById(String id) ;
}
