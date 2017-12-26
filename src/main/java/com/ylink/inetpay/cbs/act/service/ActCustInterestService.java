package com.ylink.inetpay.cbs.act.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActCustInterestDto;

public interface ActCustInterestService {

	PageData<ActCustInterestDto> findPage(PageData<ActCustInterestDto> pageData,
			ActCustInterestDto queryParam);
	
	ActCustInterestDto findById(String id);
}
