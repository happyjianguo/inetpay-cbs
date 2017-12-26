package com.ylink.inetpay.cbs.act.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActInterestDateDto;

public interface ActInterestDateService {

	public PageData<ActInterestDateDto> findPage(PageData<ActInterestDateDto> pageData, 
			ActInterestDateDto queryParam);
	
	public ActInterestDateDto findById(String id);
}
